package com.secondhand.vip

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.secondhand.vip.api.ApiClient
import com.secondhand.vip.api.ApiService
import com.secondhand.vip.model.AddProductRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddItemActivity : AppCompatActivity() {

    // ✅ 一定要在 class 裡宣告
    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDesc: EditText
    private lateinit var btnSubmit: Button

    private val api: ApiService by lazy {
        ApiClient.retrofit.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        // ✅ 這些現在才有「變數可以接」
        etName = findViewById(R.id.etName)
        etPrice = findViewById(R.id.etPrice)
        etDesc = findViewById(R.id.etDesc)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        val name = etName.text.toString().trim()
        val priceStr = etPrice.text.toString().trim()
        val desc = etDesc.text.toString().trim()

        if (name.isEmpty()) {
            toast("請輸入商品名稱")
            return
        }

        val price = priceStr.toIntOrNull()
        if (price == null) {
            toast("價格請輸入數字")
            return
        }

        val body = AddProductRequest(
            name = name,
            price = price,
            description = desc
        )

        api.addProduct(body).enqueue(object : Callback<com.secondhand.vip.model.Product> {
            override fun onResponse(
                call: Call<com.secondhand.vip.model.Product>,
                response: Response<com.secondhand.vip.model.Product>
            ) {
                if (response.isSuccessful) {
                    toast("新增成功")
                    finish()
                } else {
                    toast("新增失敗")
                }
            }

            override fun onFailure(call: Call<com.secondhand.vip.model.Product>, t: Throwable) {
                toast("連線失敗")
            }
        })
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
