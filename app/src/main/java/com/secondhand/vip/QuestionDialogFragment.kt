package com.secondhand.vip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class QuestionDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ✅ 一定要用實際存在的 layout
        return inflater.inflate(
            R.layout.dialog_add_question,
            container,
            false
        )
    }
}
