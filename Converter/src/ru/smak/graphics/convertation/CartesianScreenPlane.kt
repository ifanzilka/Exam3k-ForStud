package ru.smak.graphics.convertation

data class CartesianPlane(
    var xMin: Double,
    var xMax: Double,
    var yMin: Double,
    var yMax: Double
)

data class ScreenPlane(
    var realWidth: Int,
    var realHeight: Int
) {
    val width: Int
        get() = realWidth - 1
    val height: Int
        get() = realHeight - 1
}

data class CartesianScreenPlane(
    var realWidth: Int,
    var realHeight: Int,
    var xMin: Double,
    var xMax: Double,
    var yMin: Double,
    var yMax: Double
) {

    val width: Int
        get() = realWidth - 1
    val height: Int
        get() = realHeight - 1
    val xDen: Double
        get() = width.toDouble() / (xMax - xMin)
    val yDen: Double
        get() = height.toDouble() / (yMax - yMin)
    var screenPlane: ScreenPlane
        get() = ScreenPlane(realWidth, realHeight)
        set(value) {
            realWidth = value.realWidth
            realHeight = value.realHeight
        }
    var cartesianPlane: CartesianPlane
        get() = CartesianPlane(xMin, xMax, yMin, yMax)
        set(value) {
            xMin = value.xMin
            xMax = value.xMax
            yMin = value.yMin
            yMax = value.yMax
        }
}