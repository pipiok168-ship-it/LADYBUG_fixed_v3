package com.secondhand.vip

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // 問與答區塊（整塊可點）
        val layoutQna = findViewById<LinearLayout>(R.id.layoutQna)

        layoutQna.setOnClickListener {
            // 之後再接 Dialog，現在先確保不閃退
            // QnADialog(this).show()
        }
    }
}
