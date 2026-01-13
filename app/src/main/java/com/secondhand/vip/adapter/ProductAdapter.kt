package com.secondhand.vip.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.secondhand.vip.R
import com.secondhand.vip.model.Product

class ProductAdapter(
    private val items: List<Product>,
    private val onItemClick: (Product) -> Unit   // ✅ 點擊回傳
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txtName)
        val txtPrice: TextView = view.findViewById(R.id.txtPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = items[position]

        holder.txtName.text = product.name
        holder.txtPrice.text = "NT$ ${product.price}"

        // ✅ 正確點擊位置
        holder.itemView.setOnClickListener {
            onItemClick(product)
        }
    }
}
