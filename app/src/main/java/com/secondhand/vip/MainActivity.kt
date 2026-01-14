package com.secondhand.vip

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.secondhand.vip.auth.SellerSession

class MainActivity : AppCompatActivity() {

    private lateinit var btnBrowse: Button
    private lateinit var btnSellerLogin: Button
    private lateinit var btnAddProduct: Button
    private lateinit var btnSellerLogout: Button

    private var isSellerLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ✅ 正確的 findViewById（沒有任何奇怪字）
        btnBrowse = findViewById(R.id.btnBrowse)
        btnSellerLogin = findViewById(R.id.btnSellerLogin)
        btnAddProduct = findViewById(R.id.btnAddProduct)
        btnSellerLogout = findViewById(R.id.btnSellerLogout)

        // 查看商品列表
        btnBrowse.setOnClickListener {
            startActivity(Intent(this, ProductListActivity::class.java))
        }

        // 賣家登入
        btnSellerLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // 新增商品（已統一）
        btnAddProduct.setOnClickListener {
            if (isSellerLogin) {
                startActivity(
                    Intent(this, AddProductActivity::class.java)
                )
            } else {
                Toast.makeText(this, "請先登入賣家帳號", Toast.LENGTH_SHORT).show()
            }
        }

        // 賣家登出
        btnSellerLogout.setOnClickListener {
            SellerSession.logout(this)
            Toast.makeText(this, "已登出賣家模式", Toast.LENGTH_SHORT).show()
            refreshSellerUI()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshSellerUI()
    }

    private fun refreshSellerUI() {
        isSellerLogin = SellerSession.isLoggedIn(this)

        btnAddProduct.isEnabled = isSellerLogin
        btnSellerLogout.isEnabled = isSellerLogin
        btnSellerLogin.isEnabled = !isSellerLogin
    }
}
