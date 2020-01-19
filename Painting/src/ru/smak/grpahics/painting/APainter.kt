package ru.smak.graphics.painting

import ru.smak.graphics.convertation.CartesianScreenPlane
import java.awt.Graphics

/**
 * Абстрактный класс для сущностей,
 * осуществляющих вывод изображений на плоскость
 */
abstract class APainter : PainterEvents() {
    abstract val plane: CartesianScreenPlane
    abstract fun paint(g: Graphics?)
}