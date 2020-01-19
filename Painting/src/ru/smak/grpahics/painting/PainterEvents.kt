package ru.smak.graphics.painting

abstract class PainterEvents {

    /**
     * Обработчики событий
     */

    /**
     * Содержит набор функций, которые вызываются перед началом отрисовки объекта
     * Функции должны возвращать true, если можно рисовать или false, в случае необходимости отмены процесса
     */
    val beforePaint = mutableListOf<() -> Boolean>()
    /**
     * Содержит набор функций, которые вызываются в процессе отрисовки объекта
     * Функции должны возвращать true, если можно рисовать или false, в случае необходимости отмены процесса
     * Аргументы функции содержит числовое значение, связанное с ходом отрисовки (например, доля выполненного)
     */
    val paintStep = mutableListOf<(step: Number) -> Boolean>()
    /**
     * Содержит набор функций, которые вызываются после завершения отрисовки объекта
     * Функции должны передавать в качестве аргумента значение, означающее успешность отрисовки
     */
    val afterPaint = mutableListOf<(result: Boolean) -> Unit>()

    /**
     * Добавление обработчика события до отрисовки изображения
     * @param listener: ()->Boolean Функция, сохраняемая в качестве обработчика события
     */
    fun addBeforePaintListener(listener: () -> Boolean) {
        beforePaint.add(listener)
    }

    /**
     * Удаление обработчика события до отрисовки изображения
     * @param listener: ()->Boolean Функция, удаляемая из списка обработчиков событий
     */
    fun removeBeforePaintListener(listener: () -> Boolean) {
        beforePaint.remove(listener)
    }

    /**
     * Добавление обработчика события до отрисовки изображения
     * @param listener: (Boolean)->Unit Функция, сохраняемая в качестве обработчика события
     */
    fun addAfterPaintListener(listener: (Boolean) -> Unit) {
        afterPaint.add(listener)
    }

    /**
     * Удаление обработчика события после отрисовки изображения
     * @param listener: (Boolean)->Unit Функция, удаляемая из списка обработчиков событий
     */
    fun removeAfterPaintListener(listener: (Boolean) -> Unit) {
        afterPaint.remove(listener)
    }

    /**
     * Добавление обработчика события во время отрисовки изображения
     * @param listener: (Number)->Boolean Функция, сохраняемая в качестве обработчика события
     */
    fun addPaintStepListener(listener: (Number) -> Boolean) {
        paintStep.add(listener)
    }

    /**
     * Удаление обработчика события во время отрисовки изображения
     * @param listener: (Number)->Boolean Функция, удаляемая из списка обработчиков событий
     */
    fun removePaintStepListener(listener: (Number) -> Boolean) {
        paintStep.remove(listener)
    }
}