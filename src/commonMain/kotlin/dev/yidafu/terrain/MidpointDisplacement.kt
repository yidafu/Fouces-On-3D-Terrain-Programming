package dev.yidafu.terrain

import dev.yidafu.terrain.core.HeightMap
import dev.yidafu.terrain.core.HeightMapImpl
import dev.yidafu.terrain.core.Vertex
import dev.yidafu.terrain.ext.getVertex
import dev.yidafu.terrain.ext.randomNext
import dev.yidafu.terrain.ext.setVertex
import dev.yidafu.terrain.ext.toUByteArray
import kotlin.math.pow

/**
 * Generates terrain using the midpoint displacement (diamond-square) algorithm.
 *
 * This algorithm creates realistic-looking terrain by recursively subdividing
 * a grid and displacing the midpoint values by random amounts. The displacement
 * decreases with each iteration, controlled by the roughness parameter.
 *
 * References:
 * - https://stevelosh.com/blog/2016/02/midpoint-displacement/
 * - https://craftofcoding.wordpress.com/2021/07/09/midpoint-displacement-in-2d/
 *
 * @property roughness Controls terrain roughness (higher = more smooth, lower = more jagged)
 * @property size The grid size (must be 2^n for proper subdivision)
 */
class MidpointDisplacement(
    roughness: Double,
    size: Int = 512,
) : Terrain(size) {
    private val roughnessValue = 2.0.pow(-roughness)

    override fun generate(): HeightMap {
        val matrix = DoubleArray((size + 1) * (size + 1))
        val topLeft = Vertex(0, 0)
        val topRight = Vertex(size, 0)
        val bottomRight = Vertex(size, size)
        val bottomLeft = Vertex(0, size)
        matrix.setVertex(topLeft, size.toDouble().randomNext())
        matrix.setVertex(topRight, size.toDouble().randomNext())
        matrix.setVertex(bottomRight, size.toDouble().randomNext())
        matrix.setVertex(bottomLeft, size.toDouble().randomNext())

        calculateMidpoint(
            matrix,
            height = size.toDouble(),
            topLeft,
            topRight,
            bottomRight,
            bottomLeft,
        )

        return HeightMapImpl(size + 1, matrix.toUByteArray())
    }

    /**
     * Recursively calculates midpoints for the diamond-square algorithm.
     *
     * @param matrix The height map matrix being modified
     * @param height The current displacement height (decreases with each recursion level)
     * @param topLeft Top-left corner of the square region
     * @param topRight Top-right corner of the square region
     * @param bottomRight Bottom-right corner of the square region
     * @param bottomLeft Bottom-left corner of the square region
     */
    private fun calculateMidpoint(
        matrix: DoubleArray,
        height: Double,
        topLeft: Vertex,
        topRight: Vertex,
        bottomRight: Vertex,
        bottomLeft: Vertex,
    ) {
        val stepSize = topRight.x - topLeft.x
        if (stepSize == 1) return

        val topMid = (topRight + topLeft) / 2
        matrix.setVertex(topMid, (matrix.getVertex(topLeft) + matrix.getVertex(topRight)) / 2)

        val rightMid = (bottomRight + topRight) / 2
        matrix.setVertex(rightMid, (matrix.getVertex(topRight) + matrix.getVertex(bottomRight)) / 2)

        val bottomMid = (bottomRight + bottomLeft) / 2
        matrix.setVertex(bottomMid, (matrix.getVertex(bottomLeft) + matrix.getVertex(bottomRight)) / 2)

        val leftMid = (bottomLeft + topLeft) / 2
        matrix.setVertex(leftMid, (matrix.getVertex(topLeft) + matrix.getVertex(bottomLeft)) / 2)

        val average =
            (
                matrix.getVertex(topLeft) +
                    matrix.getVertex(topRight) +
                    matrix.getVertex(bottomRight) +
                    matrix.getVertex(bottomLeft)
            ) / 4
        val middle = (bottomRight + topLeft) / 2
        val nextValue = average + height.randomNext() * roughnessValue
        matrix.setVertex(middle, nextValue)

        calculateMidpoint(matrix, height / 2, topLeft, topMid, middle, leftMid)
        calculateMidpoint(matrix, height / 2, topMid, topRight, rightMid, middle)
        calculateMidpoint(matrix, height / 2, middle, rightMid, bottomRight, bottomMid)
        calculateMidpoint(matrix, height / 2, leftMid, middle, bottomMid, bottomLeft)
    }
}
