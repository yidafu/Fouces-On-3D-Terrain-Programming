package dev.yidafu.terrain

import de.fabmax.kool.util.Color

/**
 * Centralized constants for terrain generation and rendering.
 */
object TerrainConstants {
    // Height map scaling
    const val DEFAULT_HEIGHT_SCALE = 256f
    const val DEFAULT_HEIGHT_MULTIPLIER = 32f

    // Grid and positioning
    const val DEFAULT_GRID_SIZE = 32f
    const val DEFAULT_GRID_STEPS = 32
    const val DEFAULT_OFFSET = 0f
    const val DEFAULT_AXIS_LENGTH = 20f

    // Rendering
    const val DEFAULT_COLOR_SCALE = 16f

    // Terrain generation
    const val DEFAULT_FLATIRON_ITERATIONS = 16
    const val DEFAULT_FAULT_FORMATION_ITERATIONS = 32

    // Camera defaults
    const val DEFAULT_CAMERA_ZOOM = 30.0
    const val DEFAULT_MIN_ZOOM = 5.0
    const val DEFAULT_MAX_ZOOM = 100.0
    const val DEFAULT_CAMERA_ROTATION_X = 20f
    const val DEFAULT_CAMERA_ROTATION_Y = -30f
}

/**
 * Configuration for grid helper rendering.
 *
 * @property size The size of the grid (width and depth)
 * @property steps The number of grid lines in each direction
 * @property color The color of the grid lines
 */
data class GridConfig(
    val size: Float = TerrainConstants.DEFAULT_GRID_SIZE,
    val steps: Int = TerrainConstants.DEFAULT_GRID_STEPS,
    val color: Color = Color.GRAY.withAlpha(0.5f)
)

/**
 * Configuration for axis gizmo rendering.
 *
 * @property length The length of each axis line
 */
data class AxisConfig(
    val length: Float = TerrainConstants.DEFAULT_AXIS_LENGTH
)
