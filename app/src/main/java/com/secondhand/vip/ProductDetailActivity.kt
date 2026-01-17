package com.secondhand.vip

import android.app.Activity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.secondhand.vip.adapter.ProductImageAdapter
import com.secondhand.vip.api.ApiClient
import com.secondhand.vip.api.ApiService
import com.secondhand.vip.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var imgMain: ImageView
    private lateinit var recyclerThumbs: RecyclerView
    private lateinit var txtName: TextView
    private lateinit var txtPrice: TextView
    private lateinit var txtDescription: TextView
    private lateinit var btnContact: Button
    private lateinit var btnDelete: Button

    private lateinit var api: ApiService
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // ===== API =====
        api = ApiClient.retrofit.create(ApiService::class.java)

        // ===== Views =====
        imgMain = findViewById(R.id.imgMain)
        recyclerThumbs = findViewById(R.id.recyclerThumbs)
        txtName = findViewById(R.id.txtName)
        txtPrice = findViewById(R.id.txtPrice)
        txtDescription = findViewById(R.id.txtDescription)
        btnContact = findViewById(R.id.btnContact)
        btnDelete = findViewById(R.id.btnDelete)

        // ===== Product =====
        product = intent.getSerializableExtra("product") as Product

        txtName.text = product.name ?: ""
        txtPrice.text = "NT$ ${product.price ?: 0}"
        txtDescription.text = product.description ?: ""

        // ===== 多圖來源（正式版，向下相容）=====
        val images = mutableListOf<String>()
        if (product.imageUrls.isNotEmpty()) {
            images.addAll(product.imageUrls)
        } else {
            product.imageUrl?.let { images.add(it) }
        }

        // ===== 預設大圖 =====
        if (images.isNotEmpty()) {
            Glide.with(this)
                .load(images[0])
                .centerCrop()
                .into(imgMain)
        }

        // ===== 小圖 RecyclerView =====
        recyclerThumbs.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerThumbs.adapter = ProductImageAdapter(images) { clickedUrl ->
            Glide.with(this)
                .load(clickedUrl)
                .centerCrop()
                .into(imgMain)
        }

        // ===== 聯絡賣家（BottomSheet）=====
        btnContact.setOnClickListener {
            ContactSellerBottomSheet().show(
                supportFragmentManager,
                "ContactSellerBottomSheet"
            )
        }

        // ===== 刪除商品 =====
        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("刪除商品")
                .setMessage("確定要刪除這個商品嗎？")
                .setPositiveButton("刪除") { _, _ ->
                    product._id?.let { deleteProduct(it) }
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }

    // ===== 刪除商品 API =====
    private fun deleteProduct(id: String) {
        api.deleteProduct(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ProductDetailActivity,
                        "商品已刪除",
                        Toast.LENGTH_SHORT
                    ).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(
                        this@ProductDetailActivity,
                        "刪除失敗",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@ProductDetailActivity,
                    "連線失敗",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
