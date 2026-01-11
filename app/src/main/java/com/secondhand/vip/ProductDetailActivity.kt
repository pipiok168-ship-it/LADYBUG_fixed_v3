package com.secondhand.vip

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.secondhand.vip.adapter.ThumbAdapter
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

        product = intent.getSerializableExtra("product") as? Product
        if (product == null) {
            Toast.makeText(this, "商品資料錯誤", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        bindProduct(product!!)

        binding.btnContact.setOnClickListener { showContactDialog() }
        binding.btnDelete.setOnClickListener { confirmDelete() }
    }

    private fun bindProduct(p: Product) {
        binding.txtName.text = p.name ?: ""
        binding.txtPrice.text = "NT$ ${p.price ?: 0}"
        binding.txtDescription.text =
            p.description?.takeIf { it.isNotBlank() } ?: "（無商品說明）"

        // ✅ 組合「可用圖片清單」
        val urls = mutableListOf<String>()

        // 新：imageUrls（多圖）
        if (p.imageUrls.isNotEmpty()) {
            urls.addAll(p.imageUrls.filter { it.isNotBlank() })
        }

        // 舊：imageUrl（單圖）
        val single = p.imageUrl?.takeIf { it.isNotBlank() }
        if (single != null && urls.isEmpty()) {
            urls.add(single)
        }

        // ✅ 顯示主圖
        if (urls.isNotEmpty()) {
            showMainImage(urls[0])
        } else {
            binding.imgMain.setImageResource(R.drawable.placeholder)
        }

        // ✅ 顯示縮圖列：有 2 張以上才顯示比較合理（你也可改 >=1）
        if (urls.size >= 2) {
            binding.recyclerThumbs.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            binding.recyclerThumbs.adapter = ThumbAdapter(urls) { clickedUrl ->
                showMainImage(clickedUrl)
            }

            binding.recyclerThumbs.visibility = android.view.View.VISIBLE
        } else {
            binding.recyclerThumbs.visibility = android.view.View.GONE
        }
    }

    private fun showMainImage(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.placeholder)
            .into(binding.imgMain)
    }

    // =========================
    // 刪除商品（你原本 OK 的流程）
    // =========================
    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("確認刪除")
            .setMessage("確定要刪除此商品嗎？")
            .setPositiveButton("刪除") { _, _ -> deleteProduct() }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun deleteProduct() {
        val id = product?._id
        if (id.isNullOrBlank()) {
            Toast.makeText(this, "商品 ID 錯誤", Toast.LENGTH_SHORT).show()
            return
        }

        api.deleteProduct(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProductDetailActivity, "刪除成功", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@ProductDetailActivity, "刪除失敗 (${response.code()})", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity, "連線錯誤：${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // =========================
    // 聯絡賣家 Dialog
    // =========================
    private fun showContactDialog() {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.dialog_contact_seller, null)

        val txtPhone = view.findViewById<TextView>(R.id.txtPhone)
        val txtLineId = view.findViewById<TextView>(R.id.txtLineId)

        txtPhone.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
        }

        txtLineId.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://line.me/ti/p/~$lineId")))
        }

        AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton("關閉", null)
            .show()
    }
}
