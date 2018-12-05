package com.alirezaafkar.phuzei.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Created by Alireza Afkar on 6/12/2018AD.
 */

fun Context.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Context.toast(@StringRes stringRes: Int) =
    Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show()
