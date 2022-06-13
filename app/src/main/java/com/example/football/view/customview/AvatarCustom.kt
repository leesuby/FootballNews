package com.example.football.view.customview

import android.R
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide


class AvatarCustom : AppCompatImageView {
    constructor(context: Context, attrs: AttributeSet?,url: String) : super(context, attrs) {

        val set = intArrayOf(R.attr.background)
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, set)
        val drawable = typedArray.getDrawable(0) // 0 is an index of attrs[]

        if (drawable != null) {
            background = null
            Glide.with(context).load(url)
                .into(this) // 0 is an index of attrs[]
        }
        typedArray.recycle()
    }

}