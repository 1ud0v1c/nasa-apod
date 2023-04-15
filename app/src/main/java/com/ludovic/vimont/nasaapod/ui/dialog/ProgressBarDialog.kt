package com.ludovic.vimont.nasaapod.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.databinding.DialogProgressBarBinding

class ProgressBarDialog(activity: Activity): Dialog(activity, R.style.NumberPickerDialog) {
    fun interface OnProgressBarCancelListener {
        operator fun invoke()
    }
    var cancelClickListener: OnProgressBarCancelListener? = null

    private lateinit var binding: DialogProgressBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogProgressBarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActions()
    }

    private fun setupActions() {
        binding.buttonCancel.setOnClickListener {
            cancelClickListener?.invoke()
        }
        setOnCancelListener {
            cancelClickListener?.invoke()
        }
    }

    fun update(newProgression: Int) {
        with(binding) {
            val newText: String = context.getString(R.string.progress_bar_text_dynamic_progression, newProgression)
            textViewProgression.text = newText
            progressBar.progress = newProgression
        }
    }
}