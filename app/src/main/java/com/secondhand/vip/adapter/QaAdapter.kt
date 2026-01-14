package com.secondhand.vip.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.secondhand.vip.R
import com.secondhand.vip.model.QA

class QaAdapter(
    private val items: List<QA>
) : RecyclerView.Adapter<QaAdapter.QaViewHolder>() {

    class QaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtQuestion: TextView = view.findViewById(R.id.txtQuestion)
        val txtAnswer: TextView = view.findViewById(R.id.txtAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_qa, parent, false)
        return QaViewHolder(view)
    }

    override fun onBindViewHolder(holder: QaViewHolder, position: Int) {
        val qa = items[position]

        holder.txtQuestion.text = "Q：${qa.question}"

        holder.txtAnswer.text =
            if (qa.answer.isNullOrBlank()) {
                "A：（尚未回覆）"
            } else {
                "A：${qa.answer}"
            }
    }

    override fun getItemCount(): Int = items.size
}
