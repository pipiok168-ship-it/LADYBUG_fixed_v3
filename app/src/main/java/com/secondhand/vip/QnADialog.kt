package com.secondhand.vip

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class QnADialog(
    context: Context,
    private val questions: List<String> = emptyList()
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_qna)

        val layoutQuestions = findViewById<LinearLayout>(R.id.layoutQuestions)
        val etQuestion = findViewById<EditText>(R.id.etQuestion)
        val btnSend = findViewById<Button>(R.id.btnSend)

        // ===== 顯示問題列表 =====
        questions.forEach { q ->
            val tv = TextView(context).apply {
                text = "Q：$q"
                textSize = 14f
                setTextColor(0xFF333333.toInt())
                setPadding(24, 16, 24, 16)
                background = context.getDrawable(R.drawable.bg_qna_item)

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 8, 0, 8)
                layoutParams = params
            }
            layoutQuestions.addView(tv)
        }

        // ===== 送出問題（暫時只 Toast）=====
        btnSend.setOnClickListener {
            val text = etQuestion.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(context, "請輸入問題", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "問題已送出（尚未接後端）", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        // ===== Dialog 寬度（85% 螢幕）=====
        window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.85).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}
