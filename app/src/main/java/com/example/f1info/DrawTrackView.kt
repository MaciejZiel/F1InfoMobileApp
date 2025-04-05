package com.example.f1info

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawTrackView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val path = Path()
    private val paint = Paint().apply {
        color = 0xFF3F51B5.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 10f
        isAntiAlias = true
    }

    val trackPoints = mutableListOf<PointF>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                trackPoints.clear()
                trackPoints.add(PointF(x, y))
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                trackPoints.add(PointF(x, y))
                invalidate()
            }
        }
        return true
    }

    fun clear() {
        path.reset()
        trackPoints.clear()
        invalidate()
    }

    fun isTrackClosed(): Boolean {
        if (trackPoints.size < 10) return false

        val start = trackPoints.first()
        val end = trackPoints.last()

        val dx = start.x - end.x
        val dy = start.y - end.y

        val distance = Math.hypot(dx.toDouble(), dy.toDouble())

        // Ostrzejszy limit, np. max 15 pikseli różnicy
        return distance <= 1
    }


}
