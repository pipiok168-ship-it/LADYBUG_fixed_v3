package com.secondhand.vip.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.secondhand.vip.R

class ImagePreviewAdapter(
    private val images: MutableList<Uri>
) : RecyclerView.Adapter<ImagePreviewAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPreview: ImageView = itemView.findViewById(R.id.imgPreview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_preview, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imgPreview.setImageURI(images[position])
    }

    override fun getItemCount(): Int = images.size
}
