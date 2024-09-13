package dev.yidafu.terrain.core

expect class HeightMapRenderer(
    hMap: HeightMapImpl,
) {
    fun render()
}
