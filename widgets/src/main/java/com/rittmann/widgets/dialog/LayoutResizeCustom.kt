package com.rittmann.widgets.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ScrollView

class ScrollViewResizeCustom(context: Context, attrs: AttributeSet) : ScrollView(context, attrs) {

    private val limitHeight = AndroidUtil.getPercentHeight(context, 50.0f)
    private var adjust = false

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (adjust) {
            super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(limitHeight, MeasureSpec.EXACTLY)
            )
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onDraw(canvas: Canvas?) {
        if (height > limitHeight) {
            adjust = true
            invalidate()
            requestLayout()
        }
        super.onDraw(canvas)
    }
}
