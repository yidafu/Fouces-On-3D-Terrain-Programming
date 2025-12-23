package dev.yidafu.terrain.kool

import de.fabmax.kool.math.Vec3f
import de.fabmax.kool.scene.Node
import de.fabmax.kool.scene.addLineMesh
import de.fabmax.kool.util.Color
import dev.yidafu.terrain.AxisConfig
import dev.yidafu.terrain.GridConfig

/**
 * Adds X/Y/Z axis lines to the scene.
 *
 * @param config Configuration for axis gizmo appearance
 */
fun Node.addAxisGizmo(config: AxisConfig = AxisConfig()) = addLineMesh("AxisGizmo") {
    // X axis (Red) - points in positive X direction
    addLine(
        Vec3f.ZERO,
        Color.RED,
        Vec3f(config.length, 0f, 0f),
        Color.RED
    )

    // Y axis (Green) - points in positive Y direction
    addLine(
        Vec3f.ZERO,
        Color.GREEN,
        Vec3f(0f, config.length, 0f),
        Color.GREEN
    )

    // Z axis (Blue) - points in positive Z direction
    addLine(
        Vec3f.ZERO,
        Color.BLUE,
        Vec3f(0f, 0f, config.length),
        Color.BLUE
    )
}

/**
 * Adds a coordinate grid to the scene on the XZ plane (ground).
 *
 * @param config Configuration for grid appearance
 */
fun Node.addGrid(config: GridConfig = GridConfig()) = addGridXZ(config)

/**
 * Adds a coordinate grid on the XZ plane (ground).
 *
 * @param config Configuration for grid appearance
 */
fun Node.addGridXZ(config: GridConfig = GridConfig()) = addLineMesh("GridXZ") {
    this.color = config.color

    val halfSize = config.size / 2f
    val stepSize = config.size / config.steps

    // Draw lines parallel to X axis
    for (i in 0..config.steps) {
        val z = -halfSize + i * stepSize
        addLine(
            Vec3f(-halfSize, 0f, z),
            Vec3f(halfSize, 0f, z)
        )
    }

    // Draw lines parallel to Z axis
    for (i in 0..config.steps) {
        val x = -halfSize + i * stepSize
        addLine(
            Vec3f(x, 0f, -halfSize),
            Vec3f(x, 0f, halfSize)
        )
    }
}

/**
 * Adds a coordinate grid on the XY plane (vertical wall).
 *
 * @param config Configuration for grid appearance
 */
fun Node.addGridXY(config: GridConfig = GridConfig()) = addLineMesh("GridXY") {
    this.color = config.color

    val halfSize = config.size / 2f
    val stepSize = config.size / config.steps

    // Draw lines parallel to X axis
    for (i in 0..config.steps) {
        val y = -halfSize + i * stepSize
        addLine(
            Vec3f(-halfSize, y, 0f),
            Vec3f(halfSize, y, 0f)
        )
    }

    // Draw lines parallel to Y axis
    for (i in 0..config.steps) {
        val x = -halfSize + i * stepSize
        addLine(
            Vec3f(x, -halfSize, 0f),
            Vec3f(x, halfSize, 0f)
        )
    }
}

/**
 * Adds a coordinate grid on the YZ plane (side wall).
 *
 * @param config Configuration for grid appearance
 */
fun Node.addGridYZ(config: GridConfig = GridConfig()) = addLineMesh("GridYZ") {
    this.color = config.color

    val halfSize = config.size / 2f
    val stepSize = config.size / config.steps

    // Draw lines parallel to Y axis
    for (i in 0..config.steps) {
        val z = -halfSize + i * stepSize
        addLine(
            Vec3f(0f, -halfSize, z),
            Vec3f(0f, halfSize, z)
        )
    }

    // Draw lines parallel to Z axis
    for (i in 0..config.steps) {
        val y = -halfSize + i * stepSize
        addLine(
            Vec3f(0f, y, -halfSize),
            Vec3f(0f, y, halfSize)
        )
    }
}
