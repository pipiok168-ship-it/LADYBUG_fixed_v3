package com.secondhand.vip

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.secondhand.vip.api.ApiClient
import com.secondhand.vip.api.ApiService
import com.secondhand.vip.model.Product
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddProductActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtPrice: EditText
    private lateinit var edtDescription: EditText
    private lateinit var imgPreview: ImageView
    private lateinit var btnSelect: Button
    private lateinit var btnSubmit: Button

    private var imageUri: Uri? = null

    private val api: ApiService by lazy {
        ApiClient.retrofit.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // 綁定 View
        edtName = findViewById(R.id.edtName)
        edtPrice = findViewById(R.id.edtPrice)
        edtDescription = findViewById(R.id.edtDescription)
        imgPreview = findViewById(R.id.imgPreview)
        btnSelect = findViewById(R.id.btnSelect)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSelect.setOnClickListener { openGallery() }
        btnSubmit.setOnClickListener { uploadProduct() }
    }

    // 選擇圖片
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            Glide.with(this)
                .load(imageUri)
                .into(imgPreview)
        }
    }

    // ⭐ 關鍵：確保 description 一定送出
    private fun uploadProduct() {

        val name = edtName.text.toString().trim()
        val price = edtPrice.text.toString().trim()
        val description = edtDescription.text.toString().trim()

        if (name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "請輸入商品名稱與價格", Toast.LENGTH_SHORT).show()
            return
        }

        // ===== multipart 欄位（名稱需與後端一致）=====
        val nameBody = name.toRequestBody("text/plain".toMediaType())
        val priceBody = price.toRequestBody("text/plain".toMediaType())

        // ⭐ 就算使用者沒填，也送空字串（重點）
        val descBody = (description.ifEmpty { "" })
            .toRequestBody("text/plain".toMediaType())

        var imagePart: MultipartBody.Part? = null

        imageUri?.let { uri ->
            val file = File(cacheDir, "upload.jpg")
            contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val reqFile = file.asRequestBody("image/*".toMediaType())
            imagePart = MultipartBody.Part.createFormData(
                "image",
                file.name,
                reqFile
            )
        }

        api.addProduct(
            nameBody,
            priceBody,
            descBody,   // ✅ description 一定送
            imagePart
        ).enqueue(object : Callback<Product> {

            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(
                        this@AddProductActivity,
                        "商品上架成功",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@AddProductActivity,
                        "上架失敗",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(
                    this@AddProductActivity,
                    "連線錯誤：${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
