package com.secondhand.vip

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.secondhand.vip.model.Product

class ProductListActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        recycler = findViewById(R.id.recyclerProducts)
        recycler.layoutManager = LinearLayoutManager(this)

        // ✅ 正式 Product 假資料
        val products = listOf(
            Product(
                id = "1",
                name = "桌子",
                price = 222,
                description = "狀況良好"
            ),
            Product(
                id = "2",
                name = "椅子",
                price = 1500,
                description = "九成新"
            )
        )

        recycler.adapter = ProductAdapter(products) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }
    }
}
