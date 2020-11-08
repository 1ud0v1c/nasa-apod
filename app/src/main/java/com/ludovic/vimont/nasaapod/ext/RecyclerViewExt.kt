package com.ludovic.vimont.nasaapod.ext

import androidx.recyclerview.widget.RecyclerView

/**
 * Used to clear the space inserted while using StaggeredGridLayout style.
 */
fun RecyclerView.clearDecorations() {
    if (itemDecorationCount > 0) {
        for (index: Int in itemDecorationCount - 1 downTo 0) {
            removeItemDecorationAt(index)
        }
    }
}