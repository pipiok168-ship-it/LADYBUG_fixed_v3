package com.secondhand.vip

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.secondhand.vip.api.ApiClient
import com.secondhand.vip.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var txtName: TextView
    private lateinit var txtPrice: TextView
    private lateinit var txtDescription: TextView
    private lateinit var btnContact: Button
    private lateinit var btnDelete: Button

    private lateinit var productId: String

    private val api: ApiService by lazy {
        ApiClient.retrofit.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // ✅ 綁定 View（取代 kotlinx synthetic）
        txtName = findViewById(R.id.txtName)
        txtPrice = findViewById(R.id.txtPrice)
        txtDescription = findViewById(R.id.txtDescription)
        btnContact = findViewById(R.id.btnContact)
        btnDelete = findViewById(R.id.btnDelete)

        // ✅ 取得商品 ID
        productId = intent.getStringExtra("product_id") ?: ""
        if (productId.isEmpty()) {
            Toast.makeText(this, "商品 ID 不存在", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadProduct()

        // 聯絡賣家（只開 Dialog，不跳頁）
        btnContact.setOnClickListener {
            ContactSellerDialog().show(
                supportFragmentManager,
                "ContactSellerDialog"
            )
        }

        // 刪除商品
        btnDelete.setOnClickListener {
            confirmDelete()
        }
    }

    // ===== 取得商品資料 =====
    private fun loadProduct() {
        api.getProductById(productId).enqueue(object : Callback<com.secondhand.vip.model.Product> {
            override fun onResponse(
                call: Call<com.secondhand.vip.model.Product>,
                response: Response<com.secondhand.vip.model.Product>
            ) {
                if (!response.isSuccessful || response.body() == null) {
                    Toast.makeText(this@ProductDetailActivity, "找不到商品", Toast.LENGTH_SHORT).show()
                    finish()
                    return
                }

                val product = response.body()!!
                txtName.text = product.name
                txtPrice.text = "NT$ ${product.price}"
                txtDescription.text = product.description
            }

            override fun onFailure(call: Call<com.secondhand.vip.model.Product>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity, "連線失敗", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ===== 刪除確認 =====
    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("刪除商品")
            .setMessage("確定要刪除這個商品嗎？")
            .setPositiveButton("刪除") { _, _ ->
                deleteProduct()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    // ===== 呼叫刪除 API =====
    private fun deleteProduct() {
        api.deleteProduct(productId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(this@ProductDetailActivity, "商品已刪除", Toast.LENGTH_SHORT).show()
                finish() // 👈 回商品列表
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity, "刪除失敗", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
