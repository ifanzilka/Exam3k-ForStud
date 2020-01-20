import ru.smak.graphics.convertation.CartesianScreenPlane
import ru.smak.graphics.convertation.Converter
import ru.smak.graphics.painting.APainter
import java.awt.Color
import java.awt.Graphics
import java.lang.Thread.sleep
import kotlin.concurrent.thread
import kotlin.math.sin

class EquationParam(override val plane: CartesianScreenPlane,val x:(Double)->Double,val y:(Double)->Double): APainter(){
    override fun paint(g: Graphics?) {
        if (g!=null){
            g.color = Color.RED
            PaintTwo(g)
        }
    }
    var tMin=-50.0
    var tMax=50.0
    fun PaintTwo(g:Graphics){

        g.color = Color.gray

    thread{
        val d = (tMax - tMin) / 1000
        var t =tMin
        while (t <= tMax) {
            val x1 = Converter.xCrt2Scr(x(t), plane)
            val y1 = Converter.yCrt2Scr(y(t), plane)
            val x2 = Converter.xCrt2Scr(x(t + d), plane)
            val y2 = Converter.yCrt2Scr(y(t + d), plane)
            g.drawLine(x1, y1, x2, y2)
            sleep(10)
            t+=d
        }
        }.join()
    }


}