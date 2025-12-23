package dev.yidafu.terrain

import dev.yidafu.terrain.core.HeightMap
import dev.yidafu.terrain.core.HeightMapImpl
import dev.yidafu.terrain.core.Vertex
import dev.yidafu.terrain.ext.grid
import dev.yidafu.terrain.ext.toUByteArray
import dev.yidafu.terrain.flatiron.SimpleFlatiron
import dev.yidafu.terrain.TerrainConstants
import kotlin.random.Random

/**
 * Generates terrain using the fault formation algorithm.
 *
 * This algorithm creates terrain by repeatedly drawing random lines across
 * the height map and raising/lowering values on one side of each line.
 *
 * @property iterations Number of fault lines to apply
 * @property size Size of the height map grid
 */
class FaultFormation(
    private val iterations: Int = TerrainConstants.DEFAULT_FAULT_FORMATION_ITERATIONS,
    size: Int = 128,
) : Terrain(size) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun generate(): HeightMap {
        val tempBuffer = FloatArray(size * size) { 0.0f }
        repeat(iterations) {
            iteration(tempBuffer, it + 1, iterations, 0, 255)
        }

        SimpleFlatiron(0.6f).iron(tempBuffer)
        return HeightMapImpl(size, tempBuffer.toUByteArray())
    }

    private fun random(): Int = Random.nextInt(size)

    /**
     * Applies a single fault line iteration.
     *
     * @param tempBuffer The height map being modified
     * @param currentIteration Current iteration number (1-based)
     * @param iterations Total number of iterations
     * @param minDelta Minimum height delta
     * @param maxDelta Maximum height delta
     */
    @OptIn(ExperimentalUnsignedTypes::class)
    private fun iteration(
        tempBuffer: FloatArray,
        currentIteration: Int,
        iterations: Int,
        minDelta: Int = 0,
        maxDelta: Int = 255,
    ) {
        val height = maxDelta - ((maxDelta - minDelta) * currentIteration).toFloat() / iterations
        val v1 = Vertex(random(), random())
        val v2 = Vertex(random(), random())

        val direction = v2 - v1
        tempBuffer.toUByteArray().grid { x, z, _ ->
            val direction2 = Vertex(x, z) - v1
            if ((direction2 cross direction) > 0) {
                tempBuffer[z * size + x] += height
            }
        }
    }
}
