package com.secondhand.vip

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.secondhand.vip.api.ApiClient
import com.secondhand.vip.api.ApiService
import com.secondhand.vip.databinding.ActivityProductDetailBinding
import com.secondhand.vip.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var api: ApiService

    private var product: Product? = null

    private val phone = "0963756077"
    private val lineId = "0963756077"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = ApiClient.retrofit.create(ApiService::class.java)

        // ✅ 正確取得 Product
        product = intent.getSerializableExtra("product") as? Product

        if (product == null) {
            Toast.makeText(this, "商品資料錯誤", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // ===== 顯示資料 =====
        binding.txtName.text = product!!.name ?: ""
        binding.txtPrice.text = "NT$ ${product!!.price ?: 0}"
        binding.txtDescription.text =
            product!!.description?.takeIf { it.isNotBlank() } ?: "（無商品說明）"

        // 圖片
        product!!.imageUrl?.takeIf { it.isNotBlank() }?.let {
            Glide.with(this)
                .load(it)
                .into(binding.imgProduct)
        }

        // ===== 聯絡賣家 =====
        binding.btnContact.setOnClickListener {
            showContactDialog()
        }

        // ===== 刪除商品（關鍵）=====
        binding.btnDelete.setOnClickListener {
            confirmDelete()
        }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("刪除商品")
            .setMessage("確定要刪除此商品嗎？")
            .setPositiveButton("刪除") { _, _ ->
                deleteProduct()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun deleteProduct() {
        val productId = product?._id

        if (productId.isNullOrBlank()) {
            Toast.makeText(this, "商品 ID 錯誤", Toast.LENGTH_SHORT).show()
            return
        }

        api.deleteProduct(productId)
            .enqueue(object : Callback<Void> {

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
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
                            "刪除失敗 (${response.code()})",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        this@ProductDetailActivity,
                        "連線錯誤：${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun showContactDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_contact_seller, null)

        val txtPhone = view.findViewById<android.widget.TextView>(R.id.txtPhone)
        val txtLineId = view.findViewById<android.widget.TextView>(R.id.txtLineId)

        txtPhone.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
        }

        txtLineId.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://line.me/ti/p/~$lineId")))
        }

        AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton("關閉", null)
            .show()
    }
}
