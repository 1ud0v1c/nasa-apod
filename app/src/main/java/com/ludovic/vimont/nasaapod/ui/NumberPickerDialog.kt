package com.ludovic.vimont.nasaapod.ui

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.databinding.DialogNumberPickerBinding

class NumberPickerDialog(activity: Activity,
                         private val lastNumberOfDaysToFetch: Int): Dialog(activity, R.style.NumberPickerDialog) {
    companion object {
        const val NUMBER_PICKER_MIN_VALUE = 15
        const val NUMBER_PICKER_MAX_VALUE = 180
        const val NUMBER_PICKER_STEP = 15
    }

    private lateinit var binding: DialogNumberPickerBinding
    var onValidateClick: ((rangeOfDays: Int) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogNumberPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateNumberPicker(lastNumberOfDaysToFetch)
        setButtonActions()
    }

    private fun updateNumberPicker(numberOfDaysToFetch: Int) {
        with(binding) {
            val displayedValues: Array<String> = getDisplayedValues()
            numberPicker.minValue = 0
            numberPicker.maxValue = displayedValues.size - 1
            numberPicker.displayedValues = displayedValues
            numberPicker.value = displayedValues.indexOf("$numberOfDaysToFetch")
            numberPicker.wrapSelectorWheel = false
        }
    }

    private fun setButtonActions() {
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
        binding.buttonValidate.setOnClickListener {
            val rangeOfDays: Int = (binding.numberPicker.value + 1) * NUMBER_PICKER_STEP
            onValidateClick?.invoke(rangeOfDays)
            dismiss()
        }
    }

    private fun getDisplayedValues(): Array<String> {
        val displayedValues = ArrayList<String>()
        for (i: Int in NUMBER_PICKER_MIN_VALUE..NUMBER_PICKER_MAX_VALUE step NUMBER_PICKER_STEP) {
            displayedValues.add("$i")
        }
        return displayedValues.toTypedArray()
    }
}