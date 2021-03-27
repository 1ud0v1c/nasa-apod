package com.ludovic.vimont.nasaapod.screens.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.request.transition.Transition
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.helper.ViewHelper
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.preferences.UserPreferences

class HomePhotoAdapter(private val photos: ArrayList<Photo>): RecyclerView.Adapter<HomePhotoAdapter.PhotoViewHolder>() {
    var layout: String = UserPreferences.DEFAULT_LAYOUT
    var onItemClick: ((ImageView, Photo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        if (viewType == R.layout.item_grid_photo) {
            return GridPhotoViewHolder(itemView)
        }
        return ListPhotoViewHolder(itemView)
    }

    override fun getItemViewType(position: Int): Int {
        if (layout == UserPreferences.LAYOUT_GRID) {
            return R.layout.item_grid_photo
        }
        return R.layout.item_list_photo
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo: Photo = photos[position]
        when (getItemViewType(position)) {
            R.layout.item_list_photo -> {
                (holder as ListPhotoViewHolder).bind(photo, onItemClick)
            }
            R.layout.item_grid_photo -> {
                (holder as GridPhotoViewHolder).bind(photo, onItemClick)
            }
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    fun setItems(newPhotos: List<Photo>) {
        photos.clear()
        photos.addAll(newPhotos)
        notifyDataSetChanged()
    }

    abstract class PhotoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun loadPhoto(photo: Photo, imageView: ImageView) {
            val applicationContext: Context = itemView.context
            val cornersRadiusSize: Int = applicationContext.resources.getDimension(R.dimen.item_photo_corners_radius).toInt()
            val factory: DrawableCrossFadeFactory = DrawableCrossFadeFactory.Builder(ViewHelper.GLIDE_FADE_IN_DURATION)
                .setCrossFadeEnabled(true)
                .build()

            Glide.with(applicationContext)
                .load(photo.getImageURL())
                .placeholder(R.drawable.photo_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .transform(CenterCrop(), RoundedCorners(cornersRadiusSize))
                .into(imageView)
        }
    }

    class ListPhotoViewHolder(itemView: View): PhotoViewHolder(itemView) {
        private val imageViewPhoto: ImageView = itemView.findViewById(R.id.image_view_photo)
        private val textViewPhotoTitle: TextView = itemView.findViewById(R.id.text_view_photo_title)
        private val textViewPhotoDate: TextView = itemView.findViewById(R.id.text_view_photo_date)

        fun bind(photo: Photo, onItemClick: ((ImageView, Photo) -> Unit)?) {
            loadPhoto(photo, imageViewPhoto)
            textViewPhotoTitle.text = photo.title
            textViewPhotoDate.text = photo.getReversedDate()
            itemView.setOnClickListener {
                onItemClick?.invoke(imageViewPhoto, photo)
            }
            ViewCompat.setTransitionName(imageViewPhoto, photo.url)
        }
    }

    class GridPhotoViewHolder(itemView: View): PhotoViewHolder(itemView) {
        private val imageViewPhoto: ImageView = itemView.findViewById(R.id.image_view_photo)

        fun bind(photo: Photo, onItemClick: ((ImageView, Photo) -> Unit)?) {
            loadPhoto(photo, imageViewPhoto)
            imageViewPhoto.setOnClickListener {
                onItemClick?.invoke(imageViewPhoto, photo)
            }
            ViewCompat.setTransitionName(imageViewPhoto, photo.url)
        }
    }
}