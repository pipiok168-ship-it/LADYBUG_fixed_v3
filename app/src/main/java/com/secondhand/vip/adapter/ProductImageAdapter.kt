package com.secondhand.vip.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.secondhand.vip.R

class ProductImageAdapter(
    private val images: List<String>,                 // 圖片 URL 清單
    private val onClick: (String) -> Unit              // 點擊回呼：回傳被點的圖片
) : RecyclerView.Adapter<ProductImageAdapter.ImgVH>() {

    class ImgVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgThumb: ImageView = itemView.findViewById(R.id.imgThumb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_thumb, parent, false)
        return ImgVH(view)
    }

    override fun onBindViewHolder(holder: ImgVH, position: Int) {
        val url = images[position]

        Glide.with(holder.itemView)
            .load(url)
            .centerCrop()
            .into(holder.imgThumb)

        holder.itemView.setOnClickListener {
            onClick(url)
        }
    }

    override fun getItemCount(): Int = images.size
}
