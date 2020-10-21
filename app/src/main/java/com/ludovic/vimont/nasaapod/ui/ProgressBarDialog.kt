package com.ludovic.vimont.nasaapod.ui

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.databinding.DialogProgressBarBinding

class ProgressBarDialog(activity: Activity): Dialog(activity, R.style.NasaApodDialog) {
    private lateinit var binding: DialogProgressBarBinding
    var onCancelClick: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogProgressBarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonCancel.setOnClickListener {
            onCancelClick?.invoke()
            dismiss()
        }
    }
}