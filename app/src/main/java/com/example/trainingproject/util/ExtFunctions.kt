package com.example.trainingproject.util

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.snackbar(@StringRes errorRes: Int) {
    Snackbar.make(
        requireView(),
        errorRes,
        Snackbar.LENGTH_LONG
    ).show()
}