package com.felix.tools

import android.content.Context

/**
 * Created by zyf on 2023-05-18 18:13
 * Describe: 设备工具类
 */

/**
 * 是否是平板,宽度(长宽最短的一个)大于600dp就是平板，返回true
 */
fun Context.isTablet() = resources.getBoolean(R.bool.isTablet)
