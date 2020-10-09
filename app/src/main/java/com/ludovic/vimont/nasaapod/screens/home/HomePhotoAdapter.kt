package com.ludovic.vimont.nasaapod.screens.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.model.Photo

class HomePhotoAdapter(private val photos: ArrayList<Photo>): RecyclerView.Adapter<HomePhotoAdapter.PhotoViewHolder>() {
    companion object {
        const val FADE_IN_DURATION = 300
    }
    var onItemClick: ((Photo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_photo,
            parent,
            false
        )
        return PhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo: Photo = photos[position]
        loadPhoto(holder, photo)
        holder.textViewPhotoTitle.text = photo.title
        holder.textViewPhotoDate.text = photo.date
    }

    private fun loadPhoto(holder: PhotoViewHolder, photo: Photo) {
        val applicationContext: Context = holder.itemView.context

        val cornersRadiusSize: Int = applicationContext.resources.getDimension(R.dimen.item_photo_corners_radius).toInt()
        val factory: DrawableCrossFadeFactory = DrawableCrossFadeFactory.Builder(FADE_IN_DURATION)
            .setCrossFadeEnabled(true)
            .build()

        Glide.with(applicationContext)
            .load(photo.getImageURL())
            .placeholder(R.drawable.photo_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade(factory))
            .transform(CenterCrop(), RoundedCorners(cornersRadiusSize))
            .into(holder.imageViewPhoto)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    fun addItems(newPhotos: List<Photo>) {
        val lastPhotosSize: Int = photos.size
        photos.addAll(newPhotos)
        val newPhotosSize: Int = photos.size
        if (newPhotosSize > lastPhotosSize) {
            notifyItemRangeChanged(lastPhotosSize, newPhotosSize)
        }
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewPhoto: ImageView = itemView.findViewById(R.id.image_view_photo)
        val textViewPhotoTitle: TextView = itemView.findViewById(R.id.text_view_photo_title)
        val textViewPhotoDate: TextView = itemView.findViewById(R.id.text_view_photo_date)
    }
}