package com.anwesh.uiprojects.oppositelinearrowview

/**
 * Created by anweshmishra on 03/06/19.
 */

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import android.graphics.RectF
import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.app.Activity

val nodes : Int = 5
val lines : Int = 2
val scGap : Float = 0.05f
val scDiv : Double = 0.51
val strokeFactor : Int = 90
val sizeFactor : Float = 2.9f
val foreColor : Int = Color.parseColor("#00838F")
val backColor : Int = Color.parseColor("#BDBDBD")
val arrowFactor : Float = 3.6f
val arrowRotDeg : Float = 45f
val rFactor : Float = 3.1f

fun Int.inverse() : Float = 1f / this
fun Float.scaleFactor() : Float = Math.floor(this / scDiv).toFloat()
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.mirrorValue(a : Int, b : Int) : Float {
    val k : Float = scaleFactor()
    return (1 - k) * a.inverse() + k * b.inverse()
}
fun Float.updateValue(dir : Float, a : Int, b : Int) : Float = mirrorValue(a, b) * dir * scGap

fun Canvas.drawArrow(size : Float, paint : Paint) {
    for (j in 0..1) {
        save()
        rotate(45f * (1 - 2 * j))
        drawLine(0f, 0f, -size / arrowFactor, 0f, paint)
        restore()
    }
}

fun Canvas.drawLineArrowArc(i : Int, w : Float, size : Float, sc1 : Float, sc2 : Float, paint : Paint) {
    val r : Float = size / rFactor
    val sc1i : Float = sc1.divideScale(i, lines)
    val sc2i : Float = sc2.divideScale(i, lines)
    val x : Float = w * sc1i
    save()
    scale(1f - 2 * i, 1f - 2 * i)
    save()
    translate(-w, -r)
    drawLine(0f, 0f, x, 0f, paint)
    drawArc(RectF(-r, 0f, r, 2 * r), -90f, 180f * sc2i, false, paint)
    restore()
    save()
    rotate(180f * sc2)
    translate(-w + x, -r)
    drawArrow(size, paint)
    restore()
    restore()
}

fun Canvas.drawOLANode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = h / (nodes + 1)
    val size : Float = gap / sizeFactor
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    paint.color = foreColor
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    save()
    translate(w / 2, gap * (i + 1))
    for (j in 0..(lines - 1)) {
        drawLineArrowArc(j, w / 2, size, sc1, sc2, paint)
    }
    restore()
}

class OppositeLineArrowView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scale.updateValue(dir, lines, lines)
            if (Math.abs(this.scale - this.prevScale) > 1) {
                this.scale = this.prevScale + this.dir
                this.dir = 0f
                this.prevScale = this.scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }
}