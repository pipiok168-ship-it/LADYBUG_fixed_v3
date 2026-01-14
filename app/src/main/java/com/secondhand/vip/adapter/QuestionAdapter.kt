package com.secondhand.vip.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.secondhand.vip.R
import com.secondhand.vip.model.Question

class QuestionAdapter(
    private val items: MutableList<Question>
) : RecyclerView.Adapter<QuestionAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val txtQuestion: TextView = view.findViewById(R.id.txtQuestion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.txtQuestion.text = items[position].text
    }

    fun add(question: Question) {
        items.add(0, question)
        notifyItemInserted(0)
    }
}
