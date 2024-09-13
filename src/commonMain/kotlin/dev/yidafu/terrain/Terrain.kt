package dev.yidafu.terrain

import dev.yidafu.terrain.core.HeightMap

abstract class Terrain(
    open val size: Int,
    val heightMap: HeightMap,
) : HeightMap by heightMap {
    abstract fun render()

//    abstract fun load(filepath: String)
//
//    abstract fun save(filepath: String): Boolean

    fun unload(): Boolean = true
}

// fun SHeightData.grid(callback: (x: Int, y: Int) -> Unit) {
//    for (x in 0..<size) {
//        for (y in 0..<size) {
//            callback(x, y)
//        }
//    }
// }
