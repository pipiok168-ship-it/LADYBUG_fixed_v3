package com.secondhand.vip

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.secondhand.vip.adapter.ImageThumbAdapter
import com.secondhand.vip.model.Product

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var imgMain: ImageView
    private lateinit var recyclerThumbs: RecyclerView
    private lateinit var txtName: TextView
    private lateinit var txtPrice: TextView
    private lateinit var txtDesc: TextView
    private lateinit var imgFavorite: ImageView

    private lateinit var product: Product
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // ===== Views =====
        imgMain = findViewById(R.id.imgMain)
        recyclerThumbs = findViewById(R.id.recyclerThumbs)
        txtName = findViewById(R.id.txtName)
        txtPrice = findViewById(R.id.txtPrice)
        txtDesc = findViewById(R.id.txtDescription)
        imgFavorite = findViewById(R.id.imgFavorite)

        // ⭐ 防止點擊冒泡（關鍵）
        imgFavorite.isClickable = true
        imgFavorite.isFocusable = true

        // ===== 取得商品 =====
        product = intent.getSerializableExtra("product") as Product

        txtName.text = product.name
        txtPrice.text = "NT$ ${product.price}"
        txtDesc.text = product.description

        // ===== 圖片來源（多圖優先，單圖備援）=====
        val images =
            if (product.imageUrls.isNotEmpty()) product.imageUrls
            else listOfNotNull(product.imageUrl)

        if (images.isNotEmpty()) {
            Glide.with(this)
                .load(images[0])
                .into(imgMain)
        }

        // ===== 縮圖列表 =====
        recyclerThumbs.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerThumbs.adapter = ImageThumbAdapter(images) { url ->
            Glide.with(this).load(url).into(imgMain)
        }

        // ===== 收藏（目前 UI 狀態）=====
        updateFavoriteIcon()

        imgFavorite.setOnClickListener {
            // 吃掉點擊，不往父層傳
            it.isPressed = false

            isFavorite = !isFavorite
            updateFavoriteIcon()

            Toast.makeText(
                this,
                if (isFavorite) "已加入收藏" else "已取消收藏",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateFavoriteIcon() {
        imgFavorite.setImageResource(
            if (isFavorite)
                R.drawable.ic_favorite_border
            else
                R.drawable.ic_favorite_border
        )
    }
}
