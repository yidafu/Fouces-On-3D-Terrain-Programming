package dev.yidafu.terrain

import de.fabmax.kool.KoolContext
import de.fabmax.kool.math.Vec3f
import de.fabmax.kool.modules.ksl.KslPbrShader
import de.fabmax.kool.pipeline.CullMethod
import de.fabmax.kool.scene.addColorMesh
import de.fabmax.kool.scene.orbitCamera
import de.fabmax.kool.scene.scene
import de.fabmax.kool.util.Color
import de.fabmax.kool.util.debugOverlay
import dev.yidafu.terrain.core.HeightMap
import dev.yidafu.terrain.kool.addAxisGizmo
import dev.yidafu.terrain.kool.addGrid
import dev.yidafu.terrain.kool.addGridXY
import dev.yidafu.terrain.kool.addGridYZ
import dev.yidafu.terrain.AxisConfig
import dev.yidafu.terrain.GridConfig

/**
 * Main application launcher for the terrain demo.
 *
 * Creates a 3D scene with:
 * - Orbit camera for mouse-controlled viewing
 * - Procedurally generated terrain mesh using midpoint displacement
 * - Coordinate axis gizmo (X=Red, Y=Green, Z=Blue)
 * - Grid helpers on XZ, XY, and YZ planes
 * - Single directional light source
 *
 * @param ctx The Kool context for scene management
 */
fun launchApp(ctx: KoolContext) {
    val heightMap = MidpointDisplacement(1.5, 32).generate()

    ctx.scenes +=
        scene {
            // Enable simple camera mouse control
            orbitCamera {
                zoom = TerrainConstants.DEFAULT_CAMERA_ZOOM
                minZoom = TerrainConstants.DEFAULT_MIN_ZOOM
                maxZoom = TerrainConstants.DEFAULT_MAX_ZOOM
                setRotation(
                    TerrainConstants.DEFAULT_CAMERA_ROTATION_X,
                    TerrainConstants.DEFAULT_CAMERA_ROTATION_Y
                )
            }

            // Add terrain mesh using grid - placed on XZ plane
            addColorMesh {
                generate {
                    withTransform {
                        translate(
                            TerrainConstants.DEFAULT_OFFSET,
                            TerrainConstants.DEFAULT_OFFSET,
                            0f
                        )
                        grid {
                            sizeX = TerrainConstants.DEFAULT_GRID_SIZE
                            sizeY = TerrainConstants.DEFAULT_GRID_SIZE
                            stepsX = heightMap.size - 1
                            stepsY = heightMap.size - 1
                            xDir.set(Vec3f.X_AXIS)
                            yDir.set(Vec3f.Z_AXIS)
                            heightFun = { x, y ->
                                if (x < heightMap.size && y < heightMap.size) {
                                    val normalizedHeight =
                                        heightMap.get(x, y).toFloat() / TerrainConstants.DEFAULT_HEIGHT_SCALE
                                    normalizedHeight * TerrainConstants.DEFAULT_HEIGHT_MULTIPLIER
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
                    // Disable backface culling so terrain is visible from all angles
                    pipeline {
                        cullMethod = CullMethod.NO_CULLING
                    }
                }
            }

            // Add coordinate axis (X=Red, Y=Green, Z=Blue)
            addAxisGizmo(AxisConfig())

            // Add ground grid (XZ plane)
            addGrid(GridConfig())

            // Add XY plane grid (vertical wall)
            addGridXY(GridConfig())

            // Add YZ plane grid (side wall)
            addGridYZ(GridConfig())

            // Set up a single light source
//            lighting.singleDirectionalLight {
//                setup(Vec3f(1f, 1f, 1f))
//                setColor(Color.WHITE, 5f)
//            }
        }

    // Add the debugOverlay - provides an FPS counter and some additional debug info
    ctx.scenes += debugOverlay()
}
