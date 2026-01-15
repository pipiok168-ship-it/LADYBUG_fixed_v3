package com.secondhand.vip

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.secondhand.vip.model.Product

class AddSuccessBottomSheet(
    private val product: Product
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.bottomsheet_add_success, container, false)

        val txtName = view.findViewById<TextView>(R.id.txtName)
        val txtPrice = view.findViewById<TextView>(R.id.txtPrice)
        val txtDescription = view.findViewById<TextView>(R.id.txtDescription)
        val btnViewDetail = view.findViewById<Button>(R.id.btnViewDetail)
        val btnBackList = view.findViewById<Button>(R.id.btnBackList)

        txtName.text = product.name
        txtPrice.text = "NT$ ${product.price}"
        txtDescription.text = product.description.ifBlank { "（無商品描述）" }

        btnViewDetail.setOnClickListener {
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("product_id", product._id)
            startActivity(intent)
            dismiss()
        }

        btnBackList.setOnClickListener {
            requireActivity().setResult(android.app.Activity.RESULT_OK)
            requireActivity().finish()
            dismiss()
        }

        return view
    }
}
