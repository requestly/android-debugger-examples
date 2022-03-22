package com.cosmos.candelabra.util.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use
import androidx.fragment.app.Fragment

@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.MAGENTA)
    }
}

fun Context.themeBoolean(
    @AttrRes themeAttrId: Int
): Boolean {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getBoolean(0, false)
    }
}

fun Activity.setStatusBarColor(color: Int) {
    with(window) {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = color
    }
}

@Suppress("DEPRECATION")
fun Activity.setStatusBarTheme(light: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val appearance = if (light) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0
        window.insetsController?.setSystemBarsAppearance(
            appearance,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        val flags = if (light) {
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        window.decorView.systemUiVisibility = flags
    }
}

fun Activity.setNavigationBarColor(color: Int) {
    window.navigationBarColor = color
}

fun Fragment.isPortrait(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

fun Fragment.openExternalLink(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

    val packageManager = activity?.packageManager ?: return

    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}
