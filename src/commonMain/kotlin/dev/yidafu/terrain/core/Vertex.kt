package dev.yidafu.terrain.core

/**
 * Represents a 2D integer coordinate (vertex) on a height map grid.
 *
 * @property x X coordinate (column)
 * @property y Y coordinate (row)
 */
data class Vertex(
    val x: Int,
    val y: Int,
) {
    operator fun minus(other: Vertex): Vertex = Vertex(x - other.x, y - other.y)

    operator fun div(num: Int): Vertex = Vertex(x / num, y / num)

    operator fun plus(other: Vertex): Vertex = Vertex(x + other.x, y + other.y)

    /**
     * Computes the cross product of this vertex with another.
     *
     * For two consecutive vectors, the cross product determines the rotation direction.
     * Returns positive if the rotation from this to other is counter-clockwise.
     */
    infix fun cross(other: Vertex): Int = x * other.y - other.x * y

    override fun toString(): String = "Vertex {x: $x, y: $y}"
}
