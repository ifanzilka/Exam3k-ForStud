package ru.smak.graphics.painting

import ru.smak.graphics.convertation.CartesianScreenPlane
import ru.smak.graphics.convertation.Converter
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import kotlin.math.round

class PanelPointer(val plane: CartesianScreenPlane) {

    /**
     * Предыдущая точка отрисовки указателя
     */
    private var p: Point
    private var draw = false

    /**
     * Цвет указателя
     */
    private val color = Color.RED
    private val rectColor = Color(255, 200, 200)

    init {
        p = Point(-1, -1)
    }

    private fun paint(g: Graphics?) {
        if (!draw || p.x == -1 || p.y == -1) return
        if (g != null) {
            val y0 = Converter.yCrt2Scr(0.0, plane)
            val x0 = Converter.xCrt2Scr(0.0, plane)
            val xp = round(Converter.xScr2Crt(p.x, plane) * 100) / 100
            val yp = round(Converter.yScr2Crt(p.y, plane) * 100) / 100
            val msg = "($xp; $yp)"
            synchronized(g) {
                val m = g.fontMetrics
                val sw = m.stringWidth(msg)
                val sh = m.height
                var pt_x = -1
                var pt_y = -1
                when {
                    xp < 0 && yp >= 0 -> {
                        pt_x = p.x + 14; pt_y = p.y + 2
                    }
                    xp >= 0 && yp >= 0 -> {
                        pt_x = p.x - sw - 2; pt_y = p.y + 2
                    }
                    xp < 0 && yp < 0 -> {
                        pt_x = p.x + 2; pt_y = p.y - sh - 2
                    }
                    xp >= 0 && yp < 0 -> {
                        pt_x = p.x - sw - 2; pt_y = p.y - sh - 2
                    }
                }
                g.setXORMode(Color.WHITE)
                g.color = color
                g.drawLine(p.x, p.y, p.x, y0)
                g.drawLine(p.x, p.y, x0, p.y)
                g.color = rectColor
                g.fillRect(pt_x, pt_y, sw + 2, sh + 2)
                g.color = Color.BLACK
                g.drawString(msg, pt_x, pt_y + sh - 2)
            }
        }
    }

    fun set(e: Point, g: Graphics?) {
        draw = true
        p = e
        paint(g)
    }

    fun move(e: Point, g: Graphics?) {
        paint(g)
        p = e
        paint(g)
    }

    fun remove(g: Graphics) {
        paint(g)
        draw = false
    }
}