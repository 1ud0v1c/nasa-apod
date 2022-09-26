package com.ludovic.vimont.nasaapod.ui.gridlayout

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ludovic.vimont.nasaapod.screens.home.HomePhotoAdapter

class GridSpanSizeLookup(private val adapter: RecyclerView.Adapter<HomePhotoAdapter.PhotoViewHolder>,
                         private val gridSpanCount: Int): GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        return if (position == adapter.itemCount) gridSpanCount else 1
    }
}