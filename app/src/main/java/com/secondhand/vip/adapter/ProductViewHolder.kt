package com.secondhand.vip.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.secondhand.vip.R

class ProductViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    val image: ImageView = v.findViewById(R.id.imgThumb)
    val name: TextView = v.findViewById(R.id.txtName)
    val price: TextView = v.findViewById(R.id.txtPrice)

}
