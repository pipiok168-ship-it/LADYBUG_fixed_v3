package com.secondhand.vip.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.secondhand.vip.ProductDetailActivity
import com.secondhand.vip.R
import com.secondhand.vip.model.Product

class ProductAdapter(
    private val items: MutableList<Product>
) : RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val p = items[position]

        holder.name.text = p.name ?: ""
        holder.price.text = "NT$ ${p.price ?: 0}"

        val url = p.imageUrl?.takeIf { it.isNotBlank() }
        if (url != null) {
            Glide.with(holder.itemView.context)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }

        holder.itemView.setOnClickListener {
            val ctx = holder.itemView.context
            val itn = Intent(ctx, ProductDetailActivity::class.java)
            itn.putExtra("product", p)

            if (ctx is Activity) {
                ctx.startActivityForResult(itn, com.secondhand.vip.ProductListActivity.REQ_DETAIL)
            } else {
                ctx.startActivity(itn)
            }
        }
    }

    /**
     * ✅ 關鍵方法：給 ProductListActivity 用
     */
    fun setData(newItems: List<Product>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
