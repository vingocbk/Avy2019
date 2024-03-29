package com.app.avy.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.app.avy.R


class WeeklyViewGrid(context: Context, attrs: AttributeSet, defStyle: Int) : View(context, attrs, defStyle) {
    private val ROWS_CNT = 24
    private val COLS_CNT = 7
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    init {
        paint.color = context.resources.getColor(R.color.md_grey_800)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rowHeight = height / ROWS_CNT.toFloat()
        for (i in 0 until ROWS_CNT) {
            val y = rowHeight * i.toFloat()
          //  canvas.drawLine(0f, y, width.toFloat(), y, paint)
        }

        val rowWidth = width / COLS_CNT.toFloat()
        for (i in 1 until COLS_CNT) {
            val x = rowWidth * i.toFloat()
            canvas.drawLine(x, 0f, x, height.toFloat(), paint)
        }
    }
}
