package dev.yidafu.terrain.ext

import dev.yidafu.terrain.assert
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Calculates the width of a square array.
 * Assumes the array size is a perfect square.
 *
 * @throws AssertionError if the array size is not a perfect square
 */
@PublishedApi
internal fun calculateSquareWidth(size: Int): Int {
    val width = sqrt(size.toDouble())
    assert(width >= floor(width)) {
        "Array size must be a perfect square, got size: $size"
    }
    return width.toInt()
}
