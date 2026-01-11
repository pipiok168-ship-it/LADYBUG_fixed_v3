package com.secondhand.vip

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.secondhand.vip.api.ApiClient
import com.secondhand.vip.api.ApiService
import com.secondhand.vip.model.Product
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddProductActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtPrice: EditText
    private lateinit var edtDescription: EditText
    private lateinit var btnSelect: Button
    private lateinit var btnSubmit: Button

    private var imageUri: Uri? = null

    private val api: ApiService =
        ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        edtName = findViewById(R.id.edtName)
        edtPrice = findViewById(R.id.edtPrice)
        edtDescription = findViewById(R.id.edtDescription)
        btnSelect = findViewById(R.id.btnSelectImages)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSelect.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            startActivityForResult(intent, 1001)
        }

        btnSubmit.setOnClickListener {
            submitProduct()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
        }
    }

    private fun submitProduct() {
        val name = edtName.text.toString().trim()
        val price = edtPrice.text.toString().trim()
        val desc = edtDescription.text.toString().trim()

        if (name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "請填寫商品名稱與價格", Toast.LENGTH_SHORT).show()
            return
        }

        val nameBody = name.toRequest()
        val priceBody = price.toRequest()
        val descBody = desc.toRequest()

        var imagePart: MultipartBody.Part? = null
        if (imageUri != null) {
            val input = contentResolver.openInputStream(imageUri!!)
            val tempFile = File(cacheDir, "upload.jpg")
            tempFile.outputStream().use { out -> input?.copyTo(out) }
            val req = RequestBody.create("image/*".toMediaTypeOrNull(), tempFile)
            imagePart = MultipartBody.Part.createFormData("image", tempFile.name, req)
        }

        api.addProduct(nameBody, priceBody, descBody, imagePart)
            .enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddProductActivity, "商品新增成功", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddProductActivity, "新增失敗", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Toast.makeText(this@AddProductActivity, "連線失敗", Toast.LENGTH_SHORT).show()
                }
            })
    }
}

private fun String.toRequest(): RequestBody =
    RequestBody.create("text/plain".toMediaTypeOrNull(), this)
