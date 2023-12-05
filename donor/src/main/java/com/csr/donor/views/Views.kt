package com.csr.donor.views

import android.view.View
import android.widget.EditText


fun View.toggleEnableAndAlpha(isEnable: Boolean) {
    this.isEnabled = isEnable
    if (isEnable) {
        this.alpha = 1f
    } else {
        this.alpha = 0.3f
    }
}

fun View.toggleEnableAndVisibility(isEnable: Boolean) {
    this.isEnabled = isEnable
    if (isEnable) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

fun areEditTextsEmpty(vararg editTexts: EditText): Boolean {
    for (editText in editTexts) {
        if (editText.text.toString().trim().isEmpty()) {
            return true // At least one EditText is empty
        }
    }
    return false // All EditTexts have some text
}