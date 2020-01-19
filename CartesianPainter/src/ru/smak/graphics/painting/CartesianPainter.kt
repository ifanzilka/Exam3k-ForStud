package ru.smak.graphics.painting

import ru.smak.graphics.convertation.CartesianScreenPlane
import ru.smak.graphics.convertation.Converter
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import kotlin.math.round

enum class PlaneAxisType {
    X, Y
}

enum class LabelPosition {
    Left, Right, Top, Bottom
}

open class CartesianPainter(final override var plane: CartesianScreenPlane) : APainter() {

    var axisColor: Color = Color.GRAY
    var smallTicksColor: Color = Color.GRAY
    var largeTicksColor: Color = Color.DARK_GRAY
    var labelsColor: Color = Color.BLACK

    private var xAxis1 = 0
    private var xAxis2 = 0
    private var yAxis1 = 0
    private var yAxis2 = 0


    override fun paint(g: Graphics?) {
        if (g != null) {
            paintAxis(g as Graphics2D)
            paintTicks(g)
        }
    }

    private fun paintTicks(g: Graphics2D) {
        paintTicks(g, PlaneAxisType.X)
        paintTicks(g, PlaneAxisType.Y)
    }

    private fun paintTicks(g: Graphics2D, axis: PlaneAxisType, withLabels: Boolean = true) {
        val min = if (axis == PlaneAxisType.X) plane.xMin else plane.yMin
        val max = if (axis == PlaneAxisType.X) plane.xMax else plane.yMax
        val rmin = round(min * 10.0) / 10.0
        val rmax = round(max * 10.0) / 10.0
        var i = rmin
        val step = getStep(axis, min)
        i = round(i * round(1.0 / step)) * step

        while (i <= rmax) {
            var sz = 2
            var color = smallTicksColor
            val t = round(i * round(1 / step)).toInt()
            if (t % 5 == 0) sz += 2
            if (t % 10 == 0) {
                sz += 3; color = largeTicksColor
            }
            when (axis) {
                PlaneAxisType.X -> {
                    paintTick(
                        g, color,
                        Converter.xCrt2Scr(i, plane),
                        xAxis1,
                        axis,
                        sz
                    )
                    if (withLabels && t % 10 == 0 && t != 0) {
                        paintLabel(
                            g,
                            i.toString(),
                            Converter.xCrt2Scr(i, plane),
                            xAxis1,
                            axis,
                            LabelPosition.Bottom
                        )
                    }
                    if (xAxis1 != xAxis2) {
                        paintTick(
                            g, color,
                            Converter.xCrt2Scr(i, plane),
                            xAxis2,
                            axis,
                            sz
                        )
                        if (withLabels && t % 10 == 0 && t != 0) {
                            paintLabel(
                                g,
                                i.toString(),
                                Converter.xCrt2Scr(i, plane),
                                xAxis2,
                                axis,
                                LabelPosition.Top
                            )
                        }
                    }
                }
                PlaneAxisType.Y -> {
                    paintTick(
                        g, color,
                        yAxis1,
                        Converter.yCrt2Scr(i, plane),
                        axis,
                        sz
                    )
                    if (withLabels && t % 10 == 0 && t != 0) {
                        paintLabel(
                            g,
                            i.toString(),
                            yAxis1,
                            Converter.yCrt2Scr(i, plane),
                            axis,
                            LabelPosition.Right
                        )
                    }
                    if (yAxis1 != yAxis2) {
                        paintTick(
                            g, color,
                            yAxis2,
                            Converter.yCrt2Scr(i, plane),
                            axis,
                            sz
                        )
                        if (withLabels && t % 10 == 0 && t != 0) {
                            paintLabel(
                                g,
                                i.toString(),
                                yAxis2,
                                Converter.yCrt2Scr(i, plane),
                                axis,
                                LabelPosition.Left
                            )
                        }
                    }
                }

            }
            i = round((i + step) * 10.0) / 10.0
        }
    }

    private fun paintTick(
        g: Graphics2D, color: Color,
        xPos: Int, yPos: Int,
        axis: PlaneAxisType, size: Int
    ) {
        synchronized(g) {
            g.color = color
            when (axis) {
                PlaneAxisType.X -> g.drawLine(xPos, yPos - size, xPos, yPos + size)
                PlaneAxisType.Y -> g.drawLine(xPos - size, yPos, xPos + size, yPos)
            }
        }
    }

    private fun paintLabel(
        g: Graphics2D, value: String,
        xPos: Int, yPos: Int,
        axis: PlaneAxisType, shift: LabelPosition
    ) {
        synchronized(g) {
            g.color = labelsColor
            g.font = Font("Serief", Font.PLAIN, 10)
            val b = g.fontMetrics.getStringBounds(value, g)
            val sw = b.width
            val sh = b.height
            var size = 8
            if (shift == LabelPosition.Top || shift == LabelPosition.Left) size *= -1
            if (shift == LabelPosition.Bottom) size += sh.toInt()
            if (shift == LabelPosition.Left) size -= sw.toInt()

            when (axis) {
                PlaneAxisType.X -> g.drawString(value, xPos - (sw / 2).toInt(), yPos + size)
                PlaneAxisType.Y -> g.drawString(value, xPos + size, yPos + (sh / 2).toInt())
            }
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

    private fun paintAxis(g: Graphics2D) {
        val sx = Converter.xCrt2Scr(0.0, plane)
        val sy = Converter.yCrt2Scr(0.0, plane)
        if (sx < 0 || sx > plane.width) {
            yAxis1 = 0
            yAxis2 = plane.width
        } else {
            yAxis1 = sx
            yAxis2 = sx
        }
        if (sy < 0 || sy > plane.height) {
            xAxis1 = 0
            xAxis2 = plane.height
        } else {
            xAxis1 = sy
            xAxis2 = sy
        }
        synchronized(g) {
            g.color = axisColor
            g.drawLine(0, xAxis1, plane.width, xAxis1)
            g.drawLine(0, xAxis2, plane.width, xAxis2)
            g.drawLine(yAxis1, 0, yAxis1, plane.height)
            g.drawLine(yAxis2, 0, yAxis2, plane.height)
        }
    }
}