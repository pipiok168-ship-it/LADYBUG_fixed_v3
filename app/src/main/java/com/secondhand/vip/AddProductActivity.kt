package com.secondhand.vip

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.secondhand.vip.api.ApiClient
import com.secondhand.vip.api.ApiService
import com.secondhand.vip.model.Product
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProductActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDesc: EditText
    private lateinit var btnSubmit: Button

    private val api: ApiService =
        ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        etName = findViewById(R.id.etName)
        etPrice = findViewById(R.id.etPrice)
        etDesc = findViewById(R.id.etDesc)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            submitProduct()
        }
    }

    private fun submitProduct() {
        val name = etName.text.toString().trim()
        val price = etPrice.text.toString().trim()
        val desc = etDesc.text.toString().trim()

        if (name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "請輸入商品名稱與價格", Toast.LENGTH_SHORT).show()
            return
        }

        val nameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
        val priceBody = RequestBody.create("text/plain".toMediaTypeOrNull(), price)
        val descBody = RequestBody.create("text/plain".toMediaTypeOrNull(), desc)

        api.addProduct(nameBody, priceBody, descBody, emptyList()).enqueue(
            object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    if (response.isSuccessful && response.body() != null) {
                        showSuccessBottomSheet(response.body()!!)
                    } else {
                        Toast.makeText(this@AddProductActivity, "新增失敗", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Toast.makeText(this@AddProductActivity, "連線失敗", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    // ✅ 成功 BottomSheet（關鍵）
    private fun showSuccessBottomSheet(product: Product) {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this)
            .inflate(R.layout.sheet_add_success, null)

        view.findViewById<TextView>(R.id.txtName).text = product.name
        view.findViewById<TextView>(R.id.txtPrice).text = "NT$ ${product.price}"
        view.findViewById<TextView>(R.id.txtDesc).text = product.description

        // 👉 查看商品
        view.findViewById<Button>(R.id.btnView).setOnClickListener {
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("product_id", product._id)
            startActivity(intent)
            dialog.dismiss()
            finish()
        }

        // 👉 回列表
        view.findViewById<Button>(R.id.btnList).setOnClickListener {
            setResult(RESULT_OK)
            dialog.dismiss()
            finish()
        }

        dialog.setContentView(view)
        dialog.show()
    }
}
