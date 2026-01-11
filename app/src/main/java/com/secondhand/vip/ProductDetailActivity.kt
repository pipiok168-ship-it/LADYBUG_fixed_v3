package com.secondhand.vip

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.secondhand.vip.adapter.ProductImageAdapter
import com.secondhand.vip.model.Product

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var imgMain: ImageView
    private lateinit var recyclerThumbs: RecyclerView
    private lateinit var txtName: TextView
    private lateinit var txtPrice: TextView
    private lateinit var txtDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // ===== Views =====
        imgMain = findViewById(R.id.imgMain)
        recyclerThumbs = findViewById(R.id.recyclerThumbs)
        txtName = findViewById(R.id.txtName)
        txtPrice = findViewById(R.id.txtPrice)
        txtDescription = findViewById(R.id.txtDescription)

        // ===== 取得商品資料 =====
        val product = intent.getSerializableExtra("product") as Product

        txtName.text = product.name ?: ""
        txtPrice.text = "NT$ ${product.price ?: 0}"
        txtDescription.text = product.description ?: ""

        // ===== 圖片清單（目前用單圖模擬多圖）=====
        val images = mutableListOf<String>()
        product.imageUrl?.takeIf { it.isNotBlank() }?.let { url ->
            images.add(url)
            images.add(url) // 模擬多張
            images.add(url)
        }

        // ===== 預設顯示第一張大圖 =====
        if (images.isNotEmpty()) {
            Glide.with(this)
                .load(images.first())
                .centerCrop()
                .into(imgMain)
        }

        // ===== 小圖 RecyclerView =====
        recyclerThumbs.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerThumbs.adapter = ProductImageAdapter(images) { clickedUrl ->
            // 點小圖 → 換大圖
            Glide.with(this)
                .load(clickedUrl)
                .centerCrop()
                .into(imgMain)
        }
    }
}
