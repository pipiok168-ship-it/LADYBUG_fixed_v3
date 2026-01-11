package com.secondhand.vip

import android.app.Activity
import android.content.Intent
import android.net.Uri
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

        api = ApiClient.retrofit.create(ApiService::class.java)

        imgMain = findViewById(R.id.imgMain)
        recyclerThumbs = findViewById(R.id.recyclerThumbs)
        txtName = findViewById(R.id.txtName)
        txtPrice = findViewById(R.id.txtPrice)
        txtDescription = findViewById(R.id.txtDescription)
        btnContact = findViewById(R.id.btnContact)
        btnDelete = findViewById(R.id.btnDelete)

        product = intent.getSerializableExtra("product") as Product

        txtName.text = product.name ?: ""
        txtPrice.text = "NT$ ${product.price ?: 0}"
        txtDescription.text = product.description ?: ""

        val images = mutableListOf<String>()
        product.imageUrl?.let {
            images.add(it)
            images.add(it)
            images.add(it)
        }

        if (images.isNotEmpty()) {
            Glide.with(this).load(images[0]).centerCrop().into(imgMain)
        }

        recyclerThumbs.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerThumbs.adapter = ProductImageAdapter(images) {
            Glide.with(this).load(it).centerCrop().into(imgMain)
        }

        // 聯絡賣家
        btnContact.setOnClickListener {
            ContactSellerBottomSheet().show(
                supportFragmentManager,
                "ContactSellerBottomSheet"
            )
        }

        // 刪除商品
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
    } // ← onCreate 結束（這個就是你之前少的）

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
