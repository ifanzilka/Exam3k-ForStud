import ru.smak.graphics.convertation.CartesianScreenPlane
import ru.smak.graphics.convertation.Converter
import ru.smak.graphics.painting.APainter
import java.awt.Color
import java.awt.Graphics
import java.awt.Paint
import java.lang.reflect.Array
import kotlin.concurrent.thread
import kotlin.math.cos
import kotlin.math.sin
data class XYT(
    var X: Double,
    var Y: Double,
    var T: Double
)

class EquationPainter(override var plane: CartesianScreenPlane, val f:(Double)->Double) : APainter(){
    override fun paint(g: Graphics?) {
        if (g!=null){
            g.color = Color.blue
            PaintSleep(g)
        }
    }
    private val threads: MutableList<Thread> = mutableListOf()




    init {
    }
    fun paintsthread(g: Graphics){


        /*for (i in 0..plane.realWidth) {

            val y1=Converter.yCrt2Scr(one(Converter.xScr2Crt(i,plane)),plane)
            val y2=Converter.yCrt2Scr(one(Converter.xScr2Crt(i+1,plane)),plane)
            g.drawLine(i,y1,i+1,y2)         //линия по двум точкам все пиксели в координаты X
                }*/

        threads.clear()
        val maxThreads = 4

        for (k in 0 until maxThreads) {
            val kWidth = plane.width / maxThreads
            threads.add(k, thread {
                val min = k * kWidth
                val max = if (k == maxThreads - 1) plane.width else (k + 1) * kWidth - 1
                for (i in min..max) {


                    val y1=Converter.yCrt2Scr(f(Converter.xScr2Crt(i,plane)),plane)
                    val y2=Converter.yCrt2Scr(f(Converter.xScr2Crt(i+1,plane)),plane)
                        synchronized(g) {

                            g.drawLine(i,y1,i+1,y2)
                        }

                }
            })
        }
        for (t in threads) {
            t.join()
        }

    }

    fun PaintSleep(g: Graphics){
        g.color.red
        thread {
        for (i in 0..plane.realWidth) {

            val y1=Converter.yCrt2Scr(f(Converter.xScr2Crt(i,plane)),plane)
            val y2=Converter.yCrt2Scr(f(Converter.xScr2Crt(i+1,plane)),plane)

            g.drawLine(i,y1,i+1,y2)         //линия по двум точкам все пиксели в координаты X
            Thread.sleep(10)
        }
    }.join()

    }





    }



