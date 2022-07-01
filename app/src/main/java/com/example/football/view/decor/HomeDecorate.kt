package com.example.football.view.decor

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class HomeDecorate(context : Context, @DrawableRes dividerRes: Int) : RecyclerView.ItemDecoration() {

    private val mDivider: Drawable = ContextCompat.getDrawable(context, dividerRes)!!

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val totalHeight: Int = parent.height
        val padding = 10

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }

        val itemCount = state.itemCount

        if (itemPosition == 0) {
            outRect.set(padding, padding, 0, padding)
        } else if (itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.set(0, padding, padding, padding)
        } else {
            outRect.set(0, padding, 0, padding)
        }
    }


}