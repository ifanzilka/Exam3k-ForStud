package ru.smak.graphics.components

import ru.smak.graphics.convertation.ScreenPlane
import ru.smak.graphics.painting.APainter
import java.awt.Color
import java.awt.Graphics
import java.awt.event.*
import java.awt.image.BufferedImage
import javax.swing.JPanel

class GraphicsPanel : JPanel(), MouseListener, MouseMotionListener {

    //Свойства панели

    /**
     * Плоскость, соответствующая панели
     */
    val plane: ScreenPlane
        get() = ScreenPlane(width, height)

    /**
     * Коллекция классов-отрисовщиков чего-либо
     */
    val paintersCollection: MutableList<APainter> = mutableListOf()

    init {
        background = Color.WHITE
        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                paintersCollection.forEach {
                    it.plane.screenPlane = plane
                }
                repaint()
            }
        })
        addMouseListener(this)
        addMouseMotionListener(this)
    }

    /**
     * Добавление класса-отрисовщика графического объекта
     */
    fun addPainter(p: APainter, index: Int = -1) {
        if (index < 0) paintersCollection.add(p)
        else paintersCollection.add(index, p)
    }

    /**
     * Рисование объектов
     */
    override fun paint(g: Graphics) {
        /**
         * Изображение для формирования совокупности объектов
         */
        val bImage = BufferedImage(
            plane.realWidth,
            plane.realHeight,
            BufferedImage.TYPE_INT_RGB
        )
        val g2 = bImage.createGraphics()
        g2?.background = Color.WHITE
        g2?.clearRect(0, 0, plane.realWidth, plane.realHeight)
        super.paint(g)
        synchronized(paintersCollection) {
            paintersCollection.forEach { p ->
                g2?.let {
                    p.paint(g2)
                }
            }
        }
        synchronized(g) {
            g.drawImage(bImage, 0, 0, null)
        }
    }

    val mouseReleasedListeners = mutableListOf<(MouseEvent?) -> Unit>()
    val mouseEnteredListeners = mutableListOf<(MouseEvent?) -> Unit>()
    val mouseClickedListeners = mutableListOf<(MouseEvent?) -> Unit>()
    val mouseExitedListeners = mutableListOf<(MouseEvent?) -> Unit>()
    val mousePressedListeners = mutableListOf<(MouseEvent?) -> Unit>()
    val mouseMovedListeners = mutableListOf<(MouseEvent?) -> Unit>()
    val mouseDraggedListeners = mutableListOf<(MouseEvent?) -> Unit>()

    override fun mouseReleased(e: MouseEvent?) {
        mouseReleasedListeners.forEach { it.invoke(e) }
    }

    override fun mouseEntered(e: MouseEvent?) {
        mouseEnteredListeners.forEach { it.invoke(e) }
    }

    override fun mouseClicked(e: MouseEvent?) {
        mouseClickedListeners.forEach { it.invoke(e) }
    }

    override fun mouseExited(e: MouseEvent?) {
        mouseExitedListeners.forEach { it.invoke(e) }
    }

    override fun mousePressed(e: MouseEvent?) {
        mousePressedListeners.forEach { it.invoke(e) }
    }

    override fun mouseMoved(e: MouseEvent?) {
        mouseMovedListeners.forEach { it.invoke(e) }
    }

    override fun mouseDragged(e: MouseEvent?) {
        mouseDraggedListeners.forEach { it.invoke(e) }
    }

}