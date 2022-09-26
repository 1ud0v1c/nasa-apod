package com.ludovic.vimont.nasaapod.screens.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ludovic.vimont.nasaapod.databinding.ItemPhotoFooterBinding

class HomePhotoFooterAdapter: RecyclerView.Adapter<HomePhotoFooterAdapter.FooterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FooterViewHolder {
        val binding = ItemPhotoFooterBinding.inflate(LayoutInflater.from(parent.context))
        return FooterViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: FooterViewHolder, position: Int) = Unit

    override fun getItemCount(): Int = 1

    class FooterViewHolder(itemView: View): HomePhotoAdapter.PhotoViewHolder(itemView)
}