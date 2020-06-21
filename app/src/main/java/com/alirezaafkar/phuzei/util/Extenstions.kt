package com.alirezaafkar.phuzei.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.alirezaafkar.phuzei.MUZEI_PACKAGE_NAME
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Alireza Afkar on 6/12/2018AD.
 */

fun Context.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Context.toast(@StringRes stringRes: Int) =
    Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show()

fun Disposable.addTo(disposable: CompositeDisposable) {
    disposable.add(this)
}

fun Fragment.openInPlayStore(packageName: String) {
    var intent = requireContext().packageManager.getLaunchIntentForPackage(packageName)
    if (intent == null) {
        intent = Intent(Intent.ACTION_VIEW).apply {
            data = "market://details?id=$MUZEI_PACKAGE_NAME".toUri()
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
    startActivity(intent)
}
