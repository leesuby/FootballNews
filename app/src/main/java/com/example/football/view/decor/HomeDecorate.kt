package com.example.football.view.decor

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


//decorate for adapter home
class HomeDecorate : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

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