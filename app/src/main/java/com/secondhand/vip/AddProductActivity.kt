package com.secondhand.vip

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.secondhand.vip.adapter.ImagePreviewAdapter
import com.secondhand.vip.api.ApiClient
import com.secondhand.vip.api.ApiService
import com.secondhand.vip.model.Product
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    private lateinit var btnSelectImages: Button
    private lateinit var btnSubmit: Button
    private lateinit var recyclerImages: RecyclerView

    private val selectedUris = mutableListOf<Uri>()
    private lateinit var previewAdapter: ImagePreviewAdapter

    private val api: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    // ✅ 多選圖片（系統檔案選擇器）
    private val pickImagesLauncher =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
            selectedUris.clear()
            if (!uris.isNullOrEmpty()) {
                selectedUris.addAll(uris)
            }
            previewAdapter.notifyDataSetChanged()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        edtName = findViewById(R.id.edtName)
        edtPrice = findViewById(R.id.edtPrice)
        edtDescription = findViewById(R.id.edtDescription)
        btnSelectImages = findViewById(R.id.btnSelectImages)
        btnSubmit = findViewById(R.id.btnSubmit)
        recyclerImages = findViewById(R.id.recyclerImages)

        // ✅ 預覽 RecyclerView（橫向）
        previewAdapter = ImagePreviewAdapter(selectedUris)
        recyclerImages.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerImages.adapter = previewAdapter

        btnSelectImages.setOnClickListener {
            // 只挑圖片
            pickImagesLauncher.launch(arrayOf("image/*"))
        }

        btnSubmit.setOnClickListener {
            submitProductMulti()
        }
    }

    private fun submitProductMulti() {
        val name = edtName.text.toString().trim()
        val price = edtPrice.text.toString().trim()
        val desc = edtDescription.text.toString().trim()

        if (name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "請填寫商品名稱與價格", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedUris.isEmpty()) {
            Toast.makeText(this, "請至少選擇 1 張圖片", Toast.LENGTH_SHORT).show()
            return
        }

        val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val priceBody = price.toRequestBody("text/plain".toMediaTypeOrNull())
        val descBody = desc.toRequestBody("text/plain".toMediaTypeOrNull())

        // ✅ 轉成 Multipart（對齊後端：upload.array("images", 10)）
        val imageParts = mutableListOf<MultipartBody.Part>()

        try {
            selectedUris.forEachIndexed { index, uri ->
                val tempFile = copyUriToTempFile(uri, "upload_${System.currentTimeMillis()}_$index.jpg")
                val reqBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())

                // ⭐ 這個 key 一定要是 "images"
                val part = MultipartBody.Part.createFormData(
                    "images",
                    tempFile.name,
                    reqBody
                )
                imageParts.add(part)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "圖片處理失敗：${e.message}", Toast.LENGTH_SHORT).show()
            return
        }

        api.addProductMulti(nameBody, priceBody, descBody, imageParts)
            .enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddProductActivity, "商品新增成功", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddProductActivity, "新增失敗（${response.code()}）", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Toast.makeText(this@AddProductActivity, "連線失敗：${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // ✅ 不用 PathUtil：把 Uri 內容複製成 app cache 的暫存檔再上傳（最穩）
    private fun copyUriToTempFile(uri: Uri, fileName: String): File {
        val tempFile = File(cacheDir, fileName)
        contentResolver.openInputStream(uri).use { input ->
            requireNotNull(input) { "無法讀取圖片：$uri" }
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }
}
