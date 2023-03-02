package com.dbsh.recordapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class WaveformView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val ampList = mutableListOf<Float>()
    private val rectList = mutableListOf<RectF>()
    private val rectWidth = 15f
    private var tick = 0
    private val redPaint = Paint().apply {
        color = Color.RED
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for(rectF in rectList) {
            canvas?.drawRect(rectF, redPaint)
        }
    }

    fun addAmplitude(maxAmplitude: Float) {

        val amplitude = maxAmplitude / Short.MAX_VALUE * this.height * 0.8f // Short.Max_VALUE -> 0 ~ 1
        ampList.add(amplitude)
        rectList.clear()

        val maxRect = this.width / rectWidth
        val amps = ampList.takeLast(maxRect.toInt())

        for((i, amp) in amps.withIndex()) {
            rectList.add(
            RectF().also {
                it.top = (this.height / 2) - amp / 2
                it.bottom = it.top + amp
                it.left = i * rectWidth
                it.right = left + (rectWidth - 5f)
            })
        }

        invalidate()
    }

    fun replayAmplitude(duration: Int) {
        rectList.clear()

        val maxRect = this.width / rectWidth
        val amps = ampList.take(tick).takeLast(maxRect.toInt())

        for((i, amp) in amps.withIndex()) {
            rectList.add(
                RectF().also {
                    it.top = (this.height / 2) - amp / 2
                    it.bottom = it.top + amp
                    it.left = i * rectWidth
                    it.right = left + (rectWidth - 5f)
                })
        }

        tick++
        invalidate()
    }

    fun clearData() {
        ampList.clear()
    }

    fun clearWave() {
        rectList.clear()
        tick = 0
        invalidate()
    }
}