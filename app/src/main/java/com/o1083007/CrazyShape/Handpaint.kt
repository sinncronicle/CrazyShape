package com.o1083007.CrazyShape

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.o1083007.CrazyShape.ml.Shapes
import org.tensorflow.lite.support.image.TensorImage

class Handpaint(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)  //畫筆(避免踞齒)
    var path : Path = Path()

    init {
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE //描邊
        paint.strokeWidth = 80f  //設置畫筆寬度
        paint.strokeCap = Paint.Cap.ROUND //線帽(線條開始區域，筆觸端點)平滑
        paint.strokeJoin = Paint.Join.ROUND //連接處圓弧
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)  //背景
        canvas.drawPath(path, paint)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var xPos = event.getX()
        var yPos = event.getY()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> path.moveTo(xPos, yPos)
            MotionEvent.ACTION_MOVE -> path.lineTo(xPos, yPos)
            MotionEvent.ACTION_UP -> {

                val b = Bitmap.createBitmap(measuredWidth, measuredHeight,
                    Bitmap.Config.ARGB_8888)
                val c = Canvas(b)
                draw(c)
                classifyDrawing(b)
            }

        }
        invalidate()
        return true
    }

    fun classifyDrawing(bitmap : Bitmap) {
        val model = Shapes.newInstance(context)

        // Creates inputs for reference.
        val image = TensorImage.fromBitmap(bitmap)

        // Runs model inference and gets result.
        /*val outputs = model.process(image)
        val probability = outputs.probabilityAsCategoryList
*/
        val outputs = model.process(image)
            .probabilityAsCategoryList.apply {
                sortByDescending { it.score } // 排序，高匹配率優先
            }.take(1)  //取最高的1個

        // Releases model resources if no longer used.
        var Result:String = ""
        when (outputs[0].label) {
            "circle" -> Result = "圓形"
            "square" -> Result = "方形"
            "star" -> Result = "星形"
            "triangle" -> Result = "三角形"
        }
        Result += ": " + String.format("%.1f%%", outputs[0].score * 100.0f)

        // Releases model resources if no longer used.
        model.close()
        Toast.makeText(context, Result, Toast.LENGTH_SHORT).show()
    }
    }



