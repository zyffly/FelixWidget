package com.felix.tools

import android.content.Context
import android.widget.Toast


fun Int.toast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast =
    Toast.makeText(context, this, duration).apply {
        show()
    }

fun String.toast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast =
    Toast.makeText(context, this, duration).apply {
        show()
    }

