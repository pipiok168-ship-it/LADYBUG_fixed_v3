package com.secondhand.vip

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.secondhand.vip.auth.SellerSession

class LoginActivity : AppCompatActivity() {

    private lateinit var edtAccount: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtAccount = findViewById(R.id.edtAccount)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val acc = edtAccount.text.toString().trim()
            val pwd = edtPassword.text.toString().trim()

            if (acc.isBlank() || pwd.isBlank()) {
                Toast.makeText(this, "請輸入帳號與密碼", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 測試帳號：admin / vip2025
            if (acc == "admin" && pwd == "vip2025") {
                SellerSession.setLoggedIn(this, true)
                Toast.makeText(this, "登入成功", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
