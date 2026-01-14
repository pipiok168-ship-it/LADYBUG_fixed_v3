package com.secondhand.vip

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.secondhand.vip.api.ApiClient
import com.secondhand.vip.api.ApiService
import com.secondhand.vip.model.Product
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream

class AddProductActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDesc: EditText
    private lateinit var btnPick: Button
    private lateinit var btnSubmit: Button
    private lateinit var txtCount: TextView

    private val imageUris = mutableListOf<Uri>()

    private val api: ApiService =
        ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        etName = findViewById(R.id.etName)
        etPrice = findViewById(R.id.etPrice)
        etDesc = findViewById(R.id.etDesc)
        btnPick = findViewById(R.id.btnPickImages)
        btnSubmit = findViewById(R.id.btnSubmit)
        txtCount = findViewById(R.id.txtImageCount)

        btnPick.setOnClickListener { pickImages() }
        btnSubmit.setOnClickListener { submitProduct() }
    }

    // === 選多圖 ===
    private fun pickImages() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            imageUris.clear()

            data?.let {
                if (it.clipData != null) {
                    for (i in 0 until it.clipData!!.itemCount) {
                        imageUris.add(it.clipData!!.getItemAt(i).uri)
                    }
                } else if (it.data != null) {
                    imageUris.add(it.data!!)
                }
            }

            txtCount.text = "已選擇 ${imageUris.size} 張圖片"
        }
    }

    // === 上傳商品（關鍵修正點）===
    private fun submitProduct() {

        if (imageUris.isEmpty()) {
            Toast.makeText(this, "請至少選一張圖片", Toast.LENGTH_SHORT).show()
            return
        }

        val nameBody = etName.text.toString()
            .toRequestBody("text/plain".toMediaType())

        val priceBody = etPrice.text.toString()
            .toRequestBody("text/plain".toMediaType())

        val descBody = etDesc.text.toString()
            .toRequestBody("text/plain".toMediaType())

        val imageParts = imageUris.mapIndexed { index, uri ->
            val inputStream: InputStream =
                contentResolver.openInputStream(uri)
                    ?: throw Exception("無法讀取圖片")

            val bytes = inputStream.readBytes()
            inputStream.close()

            val requestBody = RequestBody.create(
                "image/*".toMediaType(),
                bytes
            )

            MultipartBody.Part.createFormData(
                "images",
                "image_$index.jpg",
                requestBody
            )
        }

        api.addProduct(
            nameBody,
            priceBody,
            descBody,
            imageParts
        ).enqueue(object : Callback<Product> {

            override fun onResponse(
                call: Call<Product>,
                response: Response<Product>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddProductActivity, "商品新增成功", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddProductActivity, "上傳失敗", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(this@AddProductActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}
