package com.app.avy.ui.view.seekbar

import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.drawable.Drawable

class ThumbEntity(private val centerPosition: PointF,
                  private var progress: Float,
                  private val startAngle: Float,
                  private val thumbRadius: Float,
                  private val thumbDrawable: Drawable) {

    companion object {
        private const val DEGREE_TO_RADIAN_RATIO = 0.0174533
    }

    init {
        updatePosition(progress)
    }

    fun draw(canvas: Canvas, progress: Float) {
        this.progress = progress

        updatePosition(progress)

        thumbDrawable.draw(canvas)
    }

    private fun updatePosition(progress: Float) {
        val seekbarRadius = Math.min(centerPosition.x, centerPosition.y) - thumbRadius

        val angle = (startAngle + (360 - 2 * startAngle) * progress) * DEGREE_TO_RADIAN_RATIO

        val indicatorX = centerPosition.x - Math.sin(angle) * seekbarRadius
        val indicatorY = Math.cos(angle) * seekbarRadius + centerPosition.y

        thumbDrawable.setBounds(
                (indicatorX - thumbRadius).toInt(),
                (indicatorY - thumbRadius).toInt(),
                (indicatorX + thumbRadius).toInt(),
                (indicatorY + thumbRadius).toInt())
    }
}