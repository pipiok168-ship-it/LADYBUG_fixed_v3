package com.secondhand.vip

import android.os.Bundle
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
    private val products = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerProducts.adapter = ProductAdapter(products)

        loadProducts()
    }

    private fun loadProducts() {
        val api = ApiClient.retrofit.create(ApiService::class.java)
        api.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    products.clear()
                    products.addAll(response.body() ?: emptyList())
                    binding.recyclerProducts.adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                // ❌ 先不要 Toast，不要干擾
            }
        })
    }
}
