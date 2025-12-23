package dev.yidafu.terrain.ext

import kotlin.jvm.JvmName

/**
 * Prints the array as a 2D matrix to stdout.
 * Assumes the array represents a square matrix.
 */
@OptIn(ExperimentalUnsignedTypes::class)
@JvmName("UByteArray_printMatrix")
fun UByteArray.printMatrix() {
    val size = calculateSquareWidth(this.size)
    println(" ".repeat(6) + (0..<size).joinToString("") { it.toString().padStart(6, ' ') })
    for (z in 0..<size) {
        print("$z".padStart(6, ' '))
        for (x in 0..<size) {
            print(this[z * size + x].toString().padStart(6, ' '))
        }
        println()
    }
}

/**
 * Iterates over the UByteArray as a 2D grid.
 *
 * @param callback Function called for each grid cell with x, y coordinates and value
 */
@OptIn(ExperimentalUnsignedTypes::class)
@JvmName("UByteArray_grid")
inline fun UByteArray.grid(crossinline callback: (x: Int, y: Int, value: UByte) -> Unit) {
    val size = calculateSquareWidth(this.size)
    for (x in 0..<size) {
        for (z in 0..<size) {
            val value = this[z * size + x]
            callback(x, z, value)
        }
    }
}

/**
 * Converts the UByteArray to a JSON-like string representation.
 *
 * @return String in format "[[x0,y0,v0],[x1,y1,v1],...]"
 */
@OptIn(ExperimentalUnsignedTypes::class)
@JvmName("UByteArray_toJson")
inline fun UByteArray.toJson(): String {
    val size = calculateSquareWidth(this.size)
    return buildString {
        append('[')
        var first = true
        for (y in 0..<size) {
            for (x in 0..<size) {
                if (!first) append(',')
                append("[$x,$y,${this[y * size + x]}]")
                first = false
            }
        }
        append(']')
    }
}
