package com.secondhand.vip

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.secondhand.vip.adapter.ProductAdapter
import com.secondhand.vip.api.ApiClient
import com.secondhand.vip.api.ApiService
import com.secondhand.vip.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private val products: MutableList<Product> = mutableListOf()

    private val api: ApiService by lazy {
        ApiClient.retrofit.create(ApiService::class.java)
    }

    companion object {
        const val REQ_DETAIL = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        recyclerView = findViewById(R.id.recyclerProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductAdapter(products)
        recyclerView.adapter = adapter

        loadProducts()
    }

    private fun loadProducts() {
        api.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    adapter.setData(list)
                } else {
                    Toast.makeText(
                        this@ProductListActivity,
                        "載入失敗 (${response.code()})",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(
                    this@ProductListActivity,
                    "連線失敗：${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_DETAIL && resultCode == RESULT_OK) {
            loadProducts()
        }
    }
}
