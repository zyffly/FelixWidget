package com.felix.tools

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect

/**
 * 屏幕密度
 */
val DENSITY by lazy { Resources.getSystem().displayMetrics.density }

val FONT_DENSITY by lazy { Resources.getSystem().displayMetrics.scaledDensity }

/**
 * 单位转换 dp ->px
 */
fun Float.dipToPx() = (this * DENSITY + 0.5f)
fun Int.dipToPx() = (this * DENSITY + 0.5f)

/**
 * 单位转换 px ->dp
 */
fun Float.pxToDip() = (this / DENSITY + 0.5f)
fun Int.pxToDip() = (this / DENSITY + 0.5f)

/**
 * 单位转换: sp -> px
 */
fun Int.spToPx() = (FONT_DENSITY * this + 0.5f)
fun Float.spToPx() = (FONT_DENSITY * this + 0.5f)

/**
 * 单位转换: px -> sp
 */
fun Float.pcToSp() = (this / FONT_DENSITY + 0.5f)
fun Int.pcToSp() = (this / FONT_DENSITY + 0.5f)

/**
 * 获取屏幕的宽高
 */
fun Context.displayMetrics() = resources.displayMetrics

/**
 * 获取屏幕的宽
 */
fun Context.screenWidth() = displayMetrics().widthPixels

/**
 * 获取屏幕scale的宽
 */
fun Context.scaleScreenWidth(scale: Float) = displayMetrics().widthPixels * scale

/**
 * 获取屏幕的高
 */
fun Context.screenHeight() = displayMetrics().heightPixels

/**
 * 获取屏幕scale的高
 */
fun Context.scaleScreenHeight(scale: Float) = displayMetrics().heightPixels * scale

/**
 * 获取屏幕可视区域的高度
 */
fun Activity.visibleScreenHeight(): Int {
    val outRect = Rect()
    window.decorView.getWindowVisibleDisplayFrame(outRect)
    return outRect.height()
}