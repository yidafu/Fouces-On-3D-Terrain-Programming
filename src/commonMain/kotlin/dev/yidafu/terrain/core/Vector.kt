package dev.yidafu.terrain.core

/**
 * Represents a line segment or direction vector between two vertices.
 *
 * @property v1 Starting vertex
 * @property v2 Ending vertex
 */
data class Vector(
    val v1: Vertex,
    val v2: Vertex,
)
