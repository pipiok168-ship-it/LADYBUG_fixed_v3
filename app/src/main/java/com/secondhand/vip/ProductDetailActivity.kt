package com.secondhand.vip

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.secondhand.vip.api.ApiClient
import com.secondhand.vip.api.ApiService
import com.secondhand.vip.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var txtName: TextView
    private lateinit var txtPrice: TextView
    private lateinit var txtDesc: TextView
    private lateinit var btnContactSeller: Button
    private lateinit var btnDelete: Button

    private var product: Product? = null

    private val api: ApiService by lazy {
        ApiClient.retrofit.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // ===== 綁定 View =====
        txtName = findViewById(R.id.txtName)
        txtPrice = findViewById(R.id.txtPrice)
        txtDesc = findViewById(R.id.txtDesc)
        btnContactSeller = findViewById(R.id.btnContactSeller)
        btnDelete = findViewById(R.id.btnDelete)

        // ===== 取得商品資料（從 Intent）=====
        product = intent.getSerializableExtra("product") as? Product

        if (product == null) {
            Toast.makeText(this, "商品資料不存在", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // ===== 顯示資料 =====
        txtName.text = product!!.name
        txtPrice.text = "NT$ ${product!!.price}"
        txtDesc.text = product!!.description

        // ===== 聯絡賣家（先寫死）=====
        btnContactSeller.setOnClickListener {
            showLineQrDialog()
        }

        // ===== 刪除商品 =====
        btnDelete.setOnClickListener {
            showDeleteConfirm()
        }
    }

    // ======================
    // 刪除商品流程
    // ======================
    private fun showDeleteConfirm() {
        AlertDialog.Builder(this)
            .setTitle("刪除商品")
            .setMessage("確定要刪除這個商品嗎？")
            .setPositiveButton("確定") { _, _ ->
                deleteProduct()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun deleteProduct() {
        val id = product?._id ?: return

        api.deleteProduct(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProductDetailActivity, "刪除成功", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@ProductDetailActivity, "刪除失敗", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity, "連線失敗", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ======================
    // LINE QR Code Dialog
    // ======================
    private fun showLineQrDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_line_qr, null)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("關閉", null)
            .show()
    }
}
