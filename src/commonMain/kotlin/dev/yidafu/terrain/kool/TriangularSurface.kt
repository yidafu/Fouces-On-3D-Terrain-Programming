package dev.yidafu.terrain.kool

import de.fabmax.kool.math.Vec3f
import de.fabmax.kool.scene.geometry.MeshBuilder
import de.fabmax.kool.util.Color
import de.fabmax.kool.util.setFloat3
import dev.yidafu.terrain.TerrainConstants

/**
 * Configuration properties for generating a triangular surface mesh.
 *
 * @property triangles List of vertices defining the triangles
 */
data class TriangularSurfaceProps(
    val triangles: List<Vec3f> = emptyList(),
)

/**
 * Generates a triangular surface mesh from the given properties.
 *
 * The surface is colored based on the Z-coordinate of each vertex.
 * Colors range from black (Z=0) to white (Z=16+).
 *
 * @param props Surface configuration including triangle vertices
 * @throws IllegalArgumentException if fewer than 3 vertices are provided
 */
fun MeshBuilder<*>.triangularSurface(props: TriangularSurfaceProps) {
    check(props.triangles.size >= 3) {
        "triangular surface vertex count must be >= 3, got ${props.triangles.size}"
    }

    for (tri in props.triangles) {
        val pos = Vec3f(tri.x, tri.y, tri.z * 2)
        val colorScale = tri.z / TerrainConstants.DEFAULT_COLOR_SCALE
        color = Color(colorScale, colorScale, colorScale)
        vertex {
            setFloat3(it, "attrPosition", pos)
            setFloat3(it, "attrNormal", Vec3f.Z_AXIS)
        }
    }
}
