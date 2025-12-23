package dev.yidafu.terrain.core

import dev.yidafu.terrain.ext.calculateSquareWidth

/**
 * Represents a 2D height map for terrain data.
 *
 * Height values are stored as UByte (0-255 range) and can be scaled
 * to float values for rendering using [heightScale].
 *
 * @property size The width/height of the square height map
 */
interface HeightMap {
    val size: Int

    /**
     * Gets the height value at the specified coordinates.
     *
     * @param x X coordinate (0 to size-1)
     * @param y Y coordinate (0 to size-1)
     * @return Height value as UByte (0-255)
     */
    fun get(
        x: Int,
        y: Int,
    ): UByte

    /**
     * Gets the height value at the specified vertex position.
     *
     * @param p Vertex coordinates
     * @return Height value as UByte (0-255)
     */
    fun get(p: Vertex): UByte

    /**
     * Sets the height value at the specified vertex position.
     *
     * @param p Vertex coordinates
     * @param height Height value as UByte (0-255)
     */
    fun set(
        p: Vertex,
        height: UByte,
    )

    /**
     * Sets the height value at the specified coordinates.
     *
     * @param x X coordinate (0 to size-1)
     * @param y Y coordinate (0 to size-1)
     * @param height Height value as UByte (0-255)
     */
    fun set(
        x: Int,
        y: Int,
        height: UByte,
    )

    /**
     * Sets the scale factor for converting UByte heights to Float.
     *
     * @param scale Scale multiplier
     */
    fun setHeightScale(scale: Float)

    /**
     * Gets the scaled height value at the specified coordinates.
     *
     * @param x X coordinate (0 to size-1)
     * @param y Y coordinate (0 to size-1)
     * @return Scaled height as Float
     */
    fun getScaled(
        x: Int,
        y: Int,
    ): Float
}

/**
 * Default implementation of [HeightMap] using UByteArray storage.
 *
 * @property size The width/height of the square height map
 * @property mData The underlying height data storage
 */
class HeightMapImpl(
    override val size: Int,
    @OptIn(ExperimentalUnsignedTypes::class) val mData: UByteArray = UByteArray(size),
) : HeightMap {
    private var mHeightScale: Float = 1f

    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        fun empty() = HeightMapImpl(0)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun get(
        x: Int,
        y: Int,
    ): UByte = mData.get(x, y)

    override fun get(p: Vertex): UByte = mData.get(p)

    override fun set(
        p: Vertex,
        height: UByte,
    ) = mData.set(p, height)

    override fun set(
        x: Int,
        y: Int,
        height: UByte,
    ) = mData.set(x, y, height)

    override fun setHeightScale(scale: Float) {
        mHeightScale = scale
    }

    override fun getScaled(
        x: Int,
        y: Int,
    ): Float = mData.get(x, y).toFloat() * mHeightScale
}

/**
 * Iterates over the height map as a 2D grid.
 *
 * @param cb Function called for each cell with x, y coordinates and height value
 */
inline fun HeightMap.grid(cb: (x: Int, y: Int, value: UByte) -> Unit) {
    for (y in 0..<size) {
        for (x in 0..<size) {
            cb(x, y, get(x, y))
        }
    }
}

/**
 * Maps over the height map as a 2D grid and collects results.
 *
 * @param cb Function called for each cell with x, y coordinates and height value
 * @return List of mapped results
 */
inline fun <T> HeightMap.gridMap(cb: (x: Int, y: Int, value: UByte) -> T): List<T> {
    return buildList {
        for (y in 0..<size) {
            for (x in 0..<size) {
                add(cb(x, y, get(x, y)))
            }
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
val UByteArray.width: Int
    get() = calculateSquareWidth(size)

@OptIn(ExperimentalUnsignedTypes::class)
inline fun UByteArray.set(
    x: Int,
    y: Int,
    height: UByte,
) {
    this[y * width + x] = height
}

@OptIn(ExperimentalUnsignedTypes::class)
inline fun UByteArray.set(
    p: Vertex,
    height: UByte,
) {
    this[p.y * width + p.x] = height
}

@OptIn(ExperimentalUnsignedTypes::class)
inline fun UByteArray.get(
    x: Int,
    y: Int,
): UByte = this[y * width + x]

@OptIn(ExperimentalUnsignedTypes::class)
inline fun UByteArray.get(p: Vertex): UByte = this[p.y * width + p.x]

expect fun HeightMap.saveImage(path: String)