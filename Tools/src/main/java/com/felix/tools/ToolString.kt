package com.felix.tools

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Created by FelixZhou on 2023/9/21.
 * Dsc :
 */


/**
 * 隐藏字符串
 * Hidden string
 * @param startIndex 隐藏字符串的起始位置
 *  Hide the starting position of the string
 * @param endIndex 隐藏字符串的结束位置
 *  Hide the end position of the string
 */
fun String.hideString(startIndex: Int, endIndex: Int) = when {
    startIndex >= endIndex -> this
    endIndex <= 0 -> this
    endIndex >= length -> this.substring(0, 1) + "****" + this.substring(length - 1, length)
    else -> this.substring(0, startIndex) + "****" + this.substring(endIndex, length)
}

/**
 * 隐藏手机号中间几位，适用于中国大陆
 */
fun String.hideZhCnPhoneNumber() = hideString(3, 8)

/**
 * 字符串为空或者空字符将不执行block()
 */
inline fun String?.notNullEmptyLet(block: (String) -> Unit) {
    if (this.isNullOrEmpty()) {
        return
    }
    block(this)
}

/**
 * 字符串为空或者空字符将返回指定值
 */
inline fun String?.orElseGet(block: () -> String):String {
    if (!this.isNullOrEmpty()) {
        return this
    }
    return block()
}