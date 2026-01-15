package com.secondhand.vip

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class ContactSellerDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_contact_seller, null)

        val txtPhone = view.findViewById<TextView>(R.id.txtPhone)
        val txtLineId = view.findViewById<TextView>(R.id.txtLineId)

        txtPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:0963756077")
            startActivity(intent)
        }

        txtLineId.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://line.me/ti/p/0963756077")
            startActivity(intent)
        }

        val dialog = Dialog(requireContext())
        dialog.setContentView(view)
        dialog.setCancelable(true)
        return dialog
    }
}
