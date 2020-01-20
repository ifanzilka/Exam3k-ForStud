package ru.smak.graphics.painting

import ru.smak.graphics.convertation.CartesianScreenPlane
import ru.smak.graphics.convertation.Converter
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import kotlin.math.round
import EquationPainter

enum class PlaneAxisType {
    X, Y
}

class GridPainter(override val plane: CartesianScreenPlane) : APainter() {

    private val color: Color = Color(0, 0, 0, 20)

    override fun paint(g: Graphics?) {
        g?.let { paintGrid(it as Graphics2D, PlaneAxisType.X) }
        g?.let { paintGrid(it as Graphics2D, PlaneAxisType.Y) }
    }

    private fun paintGrid(g: Graphics2D, axis: PlaneAxisType) {
        val min = if (axis == PlaneAxisType.X) plane.xMin else plane.yMin
        val max = if (axis == PlaneAxisType.X) plane.xMax else plane.yMax
        val rmin = round(min * 10.0) / 10.0
        val rmax = round(max * 10.0) / 10.0
        var i = rmin
        val step = getStep(axis, min)
        i = round(i * round(1.0 / step)) * step

        while (i <= rmax) {

            when (axis) {
                PlaneAxisType.X -> {
                    val xPos = Converter.xCrt2Scr(i, plane)
                    synchronized(g) {
                        g.color = color
                        g.drawLine(xPos, 0, xPos, plane.height)
                    }
                }
                PlaneAxisType.Y -> {
                    val yPos = Converter.yCrt2Scr(i, plane)
                    synchronized(g) {
                        g.color = color
                        g.drawLine(0, yPos, plane.width, yPos)
                    }
                }

            }
            i = round((i + step) * 10.0) / 10.0
        }


    }

    private fun getStep(axis: PlaneAxisType, minValue: Double): Double {
        //Определение шага между делениями
        val minDist = when (axis) {
            PlaneAxisType.X -> Converter.xScr2Crt(4, plane) - minValue
            PlaneAxisType.Y -> Converter.yScr2Crt(plane.height - 4, plane) - minValue
        }
        return when {
            minDist > 55.0 -> 100.0
            minDist > 11.0 -> 50.0
            minDist > 5.50 -> 10.0
            minDist > 2.10 -> 5.0
            minDist > 0.55 -> 1.0
            minDist > 0.15 -> 0.5
            else -> 0.1
        }
    }

}