package com.ludovic.vimont.nasaapod.ui.dialog

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
        setupActions()
    }

    private fun setupActions() {
        binding.buttonCancel.setOnClickListener {
            onCancelClick?.invoke()
        }
        setOnCancelListener {
            onCancelClick?.invoke()
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