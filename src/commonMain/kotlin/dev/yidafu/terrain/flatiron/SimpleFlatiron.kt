package dev.yidafu.terrain.flatiron

import dev.yidafu.terrain.TerrainConstants
import kotlin.math.sqrt

/**
 * Applies FIR (Finite Impulse Response) filtering to smooth terrain.
 *
 * This filter iterates over the terrain in four directions (top-to-bottom,
 * bottom-to-top, left-to-right, right-to-left) and applies exponential
 * smoothing to reduce sharp features.
 *
 * @property filter The smoothing coefficient (0.0 = no smoothing, 1.0 = maximum smoothing)
 */
class SimpleFlatiron(
    val filter: Float = 0.3f,
) : Flatiron {
    override fun iron(material: FloatArray) {
        val count = sqrt(material.size.toDouble()).toInt()

        repeat(TerrainConstants.DEFAULT_FLATIRON_ITERATIONS) {
            for (i in 0..<count) {
                // Vertical passes (top to bottom, bottom to top)
                firFilter(material, i, count, count, filter)
                firFilter(material, count * count - 1, -count, count, filter)

                // Horizontal passes (left to right, right to left)
                firFilter(material, i * count, 1, count, filter)
                firFilter(material, i * count + count - 1, -1, count, filter)
            }
        }
    }

    /**
     * Applies a 1D FIR filter along a line in the array.
     *
     * @param band The array to filter
     * @param start Starting index
     * @param stride Step between elements (can be negative for reverse direction)
     * @param count Number of elements to process
     * @param filter Smoothing coefficient
     */
    private fun firFilter(
        band: FloatArray,
        start: Int,
        stride: Int,
        count: Int,
        filter: Float,
    ) {
        var value = band[start]
        var i = start
        repeat(count) {
            band[i] = filter * value + (1 - filter) * band[i]
            value = band[i]
            i += stride
        }
    }
}
