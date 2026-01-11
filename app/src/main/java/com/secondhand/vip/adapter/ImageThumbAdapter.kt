package com.secondhand.vip.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.secondhand.vip.R

class ImageThumbAdapter(
    private val images: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<ImageThumbAdapter.ThumbViewHolder>() {

    inner class ThumbViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgThumb: ImageView = view.findViewById(R.id.imgThumb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_thumb, parent, false)
        return ThumbViewHolder(view)
    }

    override fun onBindViewHolder(holder: ThumbViewHolder, position: Int) {
        val url = images[position]

        Glide.with(holder.itemView.context)
            .load(url)
            .into(holder.imgThumb)

        holder.itemView.setOnClickListener {
            onClick(url)
        }
    }

    override fun getItemCount(): Int = images.size
}
