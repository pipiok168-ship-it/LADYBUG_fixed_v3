package com.secondhand.vip

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.secondhand.vip.api.ApiClient
import com.secondhand.vip.api.ApiService
import com.secondhand.vip.model.Product
import okhttp3.MediaType.Companion.toMediaTypeOrNull
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

        api.addProduct(nameBody, priceBody, descBody, emptyList())
            .enqueue(object : Callback<Product> {

                override fun onResponse(
                    call: Call<Product>,
                    response: Response<Product>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@AddProductActivity,
                            "商品新增成功",
                            Toast.LENGTH_SHORT
                        ).show()

                        // ✅ 回商品列表（不顯示任何滑上頁）
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(
                            this@AddProductActivity,
                            "新增失敗",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Toast.makeText(
                        this@AddProductActivity,
                        "連線失敗",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
