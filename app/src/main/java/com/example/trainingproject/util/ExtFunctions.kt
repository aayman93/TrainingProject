package com.example.trainingproject.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*

fun Date.getReadableDateTime() : String {
    return SimpleDateFormat("dd/MM/yyyy, h:mm a", Locale.getDefault()).format(this)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}