import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform") version "2.1.0"
    `version-catalog`
}

group = "dev.yidafu.terrain"
version = "1.0-SNAPSHOT"

val libs: LibrariesForLibs = extensions.getByType()

repositories {
    maven { setUrl("https://mirrors.cloud.tencent.com/nexus/repository/maven-public") }
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven { setUrl("https://jogamp.org/deployment/maven") }
}

kotlin {
    jvmToolchain(17)

    jvm {
        compilerOptions {
            freeCompilerArgs.add("-Xcontext-receivers")
        }
    }
    js {
        binaries.executable()
        browser {
            @OptIn(ExperimentalDistributionDsl::class)
            distribution {
                outputDirectory.set(File("${rootDir}/dist/js"))
            }
            commonWebpackConfig {
                mode =  KotlinWebpackConfig.Mode.DEVELOPMENT
            }
        }
        compilerOptions {
            target.set("es2015")
            freeCompilerArgs.add("-Xcontext-receivers")
        }
    }

    sourceSets {
        val targetPlatforms = listOf("natives-windows", "natives-linux", "natives-macos", "natives-macos-arm64")

        commonTest.dependencies {
//            implementation(kotlin("test")) // Brings all the platform dependencies automatically
        }

        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.bundles.kool)
            implementation(libs.kotlinx.coroutines.core)
        }
        val jsMain by getting {

            dependencies {
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.bundles.jogamp)
                implementation(libs.joml)

                targetPlatforms.forEach { platform ->
                    runtimeOnly("org.lwjgl:lwjgl:${libs.versions.lwjgl.get()}:$platform")
                    val hasVulkanRuntime = "macos" in platform
                    listOf("glfw", "vulkan", "jemalloc", "nfd", "stb", "vma", "shaderc")
                        .filter { it != "vulkan" || hasVulkanRuntime }
                        .forEach { lib ->
                            runtimeOnly("org.lwjgl:lwjgl-$lib:${libs.versions.lwjgl.get()}:$platform")
                        }

                    // physx-jni runtime libs
                    runtimeOnly("de.fabmax:physx-jni:${libs.versions.physx.jni.get()}:$platform")
                }
            }
        }
    }
}

task("runnableJar", Jar::class) {
    dependsOn("jvmJar")

    group = "app"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveAppendix.set("runnable")
    manifest {
        attributes["Main-Class"] = "LauncherKt"
    }

    configurations
        .asSequence()
        .filter { it.name.startsWith("common") || it.name.startsWith("jvm") }
        .map { it.copyRecursive().fileCollection { true } }
        .flatten()
        .distinct()
        .filter { it.exists() }
        .map { if (it.isDirectory) it else zipTree(it) }
        .forEach { from(it) }
    from(layout.buildDirectory.files("classes/kotlin/jvm/main"))

    doLast {
        copy {
            from(layout.buildDirectory.file("libs/${archiveBaseName.get()}-runnable.jar"))
            into("$rootDir/dist/jvm")
        }
    }
}

task("runApp", JavaExec::class) {
    group = "app"
    dependsOn("jvmMainClasses")

    classpath = layout.buildDirectory.files("classes/kotlin/jvm/main")
    configurations
        .filter { it.name.startsWith("common") || it.name.startsWith("jvm") }
        .map { it.copyRecursive().filter { true } }
        .forEach { classpath += it }

    mainClass.set("LauncherKt")
    if (OperatingSystem.current().isMacOsX) {
        jvmArgs = listOf("-XstartOnFirstThread")
    }
}

val build by tasks.getting(Task::class) {
    dependsOn("runnableJar")
}

val clean by tasks.getting(Task::class) {
    doLast {
        delete("$rootDir/dist")
    }
}
