package com.ludovic.vimont.nasaapod.ext

import androidx.annotation.IntRange
import androidx.fragment.app.Fragment

fun Fragment.requestPermission(vararg permissions: String, @IntRange(from = 0) requestCode: Int) =
    requestPermissions(permissions, requestCode)