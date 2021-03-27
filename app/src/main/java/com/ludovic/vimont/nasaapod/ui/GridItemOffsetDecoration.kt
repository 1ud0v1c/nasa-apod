package com.ludovic.vimont.nasaapod.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

/**
 * Used to have an equals spacing between the items of the GridLayoutManager.
 */
class GridItemOffsetDecoration(private val spanCount: Int,
                               private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        val params: GridLayoutManager.LayoutParams? = view.layoutParams as? GridLayoutManager.LayoutParams
        val spanIndex: Int = params?.spanIndex ?: 0
        val position: Int = params?.viewAdapterPosition ?: 0

        outRect.left = if (spanIndex == 0) spacing else 0
        outRect.top = if (position < spanCount) spacing else 0
        outRect.right = spacing
        outRect.bottom = spacing
    }
}