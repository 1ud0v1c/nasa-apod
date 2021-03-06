package com.ludovic.vimont.nasaapod.screens.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ludovic.vimont.nasaapod.R

class HomePhotoFooterAdapter: RecyclerView.Adapter<HomePhotoFooterAdapter.FooterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FooterViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_footer, parent, false)
        return FooterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FooterViewHolder, position: Int) {
        // Nothing to do here
    }

    override fun getItemCount(): Int {
        return 1
    }

    class FooterViewHolder(itemView: View): HomePhotoAdapter.PhotoViewHolder(itemView)
}