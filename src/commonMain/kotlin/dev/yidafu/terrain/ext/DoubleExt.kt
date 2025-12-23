package dev.yidafu.terrain.ext

import kotlin.random.Random

/**
 * Generates a random displacement value centered around zero.
 *
 * The returned value is uniformly distributed in the range [-this/2, this/2).
 *
 * @return A random displacement value
 */
internal inline fun Double.randomNext(): Double {
    return Random.nextDouble(-this / 2, this / 2)
}
