package dev.yidafu.terrain

import dev.yidafu.terrain.core.HeightMap

/**
 * Abstract base class for terrain generation algorithms.
 *
 * All terrain generators extend this class and implement the [generate] method
 * to produce a [HeightMap].
 *
 * @property size The width/height of the square height map to generate
 */
abstract class Terrain(
    open val size: Int,
) {
    /**
     * Generates a terrain height map.
     *
     * @return A new HeightMap with the generated terrain data
     */
    abstract fun generate(): HeightMap

    /**
     * Unloads the terrain resources.
     *
     * @return true if unloading was successful
     */
    fun unload(): Boolean = true
}
