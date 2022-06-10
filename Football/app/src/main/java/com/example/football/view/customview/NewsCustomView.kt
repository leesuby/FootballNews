package com.example.football.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View


class NewsCustomView : View {
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    init {

    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.RED)


    }


}