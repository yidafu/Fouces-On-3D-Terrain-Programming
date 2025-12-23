package dev.yidafu.terrain.ext

import dev.yidafu.terrain.core.Vertex
import kotlin.jvm.JvmName
import kotlin.math.floor

/**
 * Converts a DoubleArray to UByteArray by normalizing values to 0-100 range.
 *
 * The conversion maps the input range [min, max] to [0, 100].
 *
 * @return UByteArray with normalized values in range 0-100
 */
@OptIn(ExperimentalUnsignedTypes::class)
@JvmName("DoubleArray_toUByteArray")
inline fun DoubleArray.toUByteArray(): UByteArray {
    val maxValue = this.max()
    val minValue = this.min()
    val range = maxValue - minValue
    return map {
        floor(((it - minValue) / range * 100)).toInt().toUByte()
    }.toUByteArray()
}

/**
 * Prints the array as a 2D matrix to stdout.
 * Assumes the array represents a square matrix.
 */
@JvmName("DoubleArray_printMatrix")
fun DoubleArray.printMatrix() {
    val size = width
    for (x in 0..<size) {
        for (z in 0..<size) {
            print(this[z * size + x].toString().padStart(8, ' '))
        }
        println()
    }
}

val DoubleArray.width: Int
    get() = calculateSquareWidth(size)

@JvmName("DoubleArray_setVertex")
inline fun DoubleArray.setVertex(
    vertex: Vertex,
    value: Double,
) {
    val w = width
    this[vertex.y * w + vertex.x] = value
}

@JvmName("DoubleArray_getVertex")
inline fun DoubleArray.getVertex(vertex: Vertex) = this[vertex.y * width + vertex.x]
