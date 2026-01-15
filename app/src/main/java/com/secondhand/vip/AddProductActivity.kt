package com.secondhand.vip

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
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

    // 📸 選擇多圖
    private val pickImagesLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            imageUris.clear()
            imageUris.addAll(uris.take(5))
            txtCount.text = "已選擇 ${imageUris.size} / 5 張圖片"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        etName = findViewById(R.id.etName)
        etPrice = findViewById(R.id.etPrice)
        etDesc = findViewById(R.id.etDesc)
        btnPick = findViewById(R.id.btnPick)          // ✅ 關鍵修正
        btnSubmit = findViewById(R.id.btnSubmit)
        txtCount = findViewById(R.id.txtCount)

        btnPick.setOnClickListener {
            pickImagesLauncher.launch("image/*")
        }

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

        val nameBody = name.toRequestBody()
        val priceBody = price.toRequestBody()
        val descBody = desc.toRequestBody()

        val imageParts = imageUris.mapIndexed { index, uri ->
            val inputStream: InputStream = contentResolver.openInputStream(uri)!!
            val bytes = inputStream.readBytes()
            val body = RequestBody.create("image/*".toMediaTypeOrNull(), bytes)
            MultipartBody.Part.createFormData("images", "image_$index.jpg", body)
        }

        api.addProduct(nameBody, priceBody, descBody, imageParts)
            .enqueue(object : Callback<Product> {
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
            })
    }

    private fun showSuccessBottomSheet(product: Product) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.sheet_add_success, null)

        view.findViewById<TextView>(R.id.txtName).text = product.name
        view.findViewById<TextView>(R.id.txtPrice).text = "NT$ ${product.price}"
        view.findViewById<TextView>(R.id.txtDescription).text = product.description

        view.findViewById<Button>(R.id.btnViewDetail).setOnClickListener {
            startActivity(
                Intent(this, ProductDetailActivity::class.java)
                    .putExtra("product_id", product._id)
            )
            dialog.dismiss()
            finish()
        }

        view.findViewById<Button>(R.id.btnList).setOnClickListener {
            setResult(RESULT_OK)
            dialog.dismiss()
            finish()
        }

        dialog.setContentView(view)
        dialog.show()
    }
}

// 小工具
private fun String.toRequestBody(): RequestBody =
    RequestBody.create("text/plain".toMediaTypeOrNull(), this)
