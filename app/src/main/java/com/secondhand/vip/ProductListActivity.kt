package com.secondhand.vip

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.secondhand.vip.adapter.ProductAdapter
import com.secondhand.vip.api.ApiClient
import com.secondhand.vip.api.ApiService
import com.secondhand.vip.databinding.ActivityProductListBinding
import com.secondhand.vip.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var adapter: ProductAdapter
    private val products = mutableListOf<Product>()

    private val api: ApiService =
        ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ 關鍵修正在這：明確給 lambda
        adapter = ProductAdapter(products) { product ->
            openProductDetail(product)
        }

        binding.recyclerProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerProducts.adapter = adapter

        loadProducts()
    }

    private fun loadProducts() {
        api.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    products.clear()
                    products.addAll(response.body() ?: emptyList())
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@ProductListActivity, "讀取失敗", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@ProductListActivity, "連線錯誤", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openProductDetail(product: Product) {
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }
}
