package dev.yidafu.terrain.ext

import dev.yidafu.terrain.core.Vertex
import kotlin.jvm.JvmName

/**
 * Converts a FloatArray to UByteArray by normalizing values to 0-255 range.
 *
 * The conversion maps the input range [min, max] to [0, 255].
 *
 * @return UByteArray with normalized values in range 0-255
 */
@OptIn(ExperimentalUnsignedTypes::class)
@JvmName("FloatArray_toUByteArray")
inline fun FloatArray.toUByteArray(): UByteArray {
    val maxValue = this.max()
    val minValue = this.min()
    val range = maxValue - minValue
    return map {
        ((it - minValue) / range * 255).toInt().toUByte()
    }.toUByteArray()
}

/**
 * Iterates over the FloatArray as a 2D grid.
 *
 * @param callback Function called for each grid cell with x, y coordinates and value
 */
@JvmName("FloatArray_grid")
inline fun FloatArray.grid(crossinline callback: (x: Int, y: Int, value: Float) -> Unit) {
    val size = width
    for (x in 0..<size) {
        for (z in 0..<size) {
            val value = this[z * size + x]
            callback(x, z, value)
        }
    }
}

/**
 * Prints the array as a 2D matrix to stdout.
 * Assumes the array represents a square matrix.
 */
@JvmName("FloatArray_printMatrix")
fun FloatArray.printMatrix() {
    val size = width
    for (x in 0..<size) {
        for (z in 0..<size) {
            print(this[z * size + x].toString().padStart(8, ' '))
        }
        println()
    }
}

val FloatArray.width: Int
    get() = calculateSquareWidth(size)

@JvmName("FloatArray_setVertex")
inline fun FloatArray.setVertex(
    vertex: Vertex,
    value: Float,
) {
    val w = width
    this[vertex.y * w + vertex.x] = value
}

@JvmName("FloatArray_getVertex")
inline fun FloatArray.getVertex(vertex: Vertex) = this[vertex.y * width + vertex.x]
