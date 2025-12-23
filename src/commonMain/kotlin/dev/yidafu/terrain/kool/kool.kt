package dev.yidafu.terrain.kool

import de.fabmax.kool.math.Vec3f
import de.fabmax.kool.modules.ksl.KslPbrShader
import de.fabmax.kool.scene.Node
import de.fabmax.kool.scene.Node.Companion.makeNodeName
import de.fabmax.kool.scene.addColorMesh

fun Node.addTriangulatedMesh(
    name: String = makeNodeName("TriangularSurfaceMesh"),
    points: List<Vec3f>,
) = addColorMesh {
    generate {
        withTransform {
            translate(-8f, -8f, 0f)
            // Points come in pairs: (v1, v2, v1, v2, ...) for a triangle strip
            // We need to convert them to a proper grid
            val gridSize = (points.size / 2) - 1

            grid {
                sizeX = 32f
                sizeY = 32f
                stepsX = gridSize
                stepsY = 1
                xDir.set(Vec3f.X_AXIS)
                yDir.set(Vec3f.NEG_Z_AXIS)
                heightFun = { x, y ->
                    val idx = x * 2 + y
                    if (idx < points.size) {
                        points[idx].z * 16f
                    } else {
                        0f
                    }
                }
            }
        }
    }
    shader = KslPbrShader {
        color { vertexColor() }
        metallic(0f)
        roughness(0.25f)
    }
}
