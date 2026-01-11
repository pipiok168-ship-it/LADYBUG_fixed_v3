package com.secondhand.vip.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.secondhand.vip.R

object ContactSellerDialog {

    private const val PHONE = "0963756077"
    private const val LINE_ID = "0963756077"

    fun show(context: Context) {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialog_contact_seller, null)

        val txtPhone = view.findViewById<TextView>(R.id.txtPhone)
        val txtLine = view.findViewById<TextView>(R.id.txtLineId)

        // ===== 電話（你現在已 OK，不動）=====
        txtPhone.text = "撥打賣家電話\n$PHONE"

        // ===== LINE ID（本次重點）=====
        txtLine.text = "LINE ID：$LINE_ID"
        txtLine.setTextColor(Color.parseColor("#00B900")) // LINE 綠

        txtLine.setOnClickListener {
            openLine(context, LINE_ID)
        }

        AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(true)
            .setPositiveButton("關閉", null)
            .show()
    }

    private fun openLine(context: Context, lineId: String) {
        val uri = Uri.parse("https://line.me/ti/p/~$lineId")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // 沒有 LINE → Play 商店
            val storeIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=jp.naver.line.android")
            )
            context.startActivity(storeIntent)
        }
    }
}
