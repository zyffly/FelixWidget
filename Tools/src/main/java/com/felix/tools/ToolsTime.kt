package com.felix.tools

import androidx.annotation.StringDef
import java.text.SimpleDateFormat
import java.util.*


private const val MS_HOUR = 1000L * 60 * 60
private const val MS_MINUTE = 1000L * 60
private const val MS_SECOND = 1000L

/**
 * 公用状态，包含开关状态、是否等等
 */
@StringDef(
    DateType.LINE_Y_M_D_H_M_S_S,
    DateType.LINE_Y_M_D_H_M_S,
    DateType.LINE_Y_M_D_H_M,
    DateType.LINE_Y_M_D,
    DateType.LINE_M_D_H_M,
    DateType.SLASH_Y_M_D_H_M_S,
    DateType.SLASH_Y_M_D_H_M,
    DateType.SLASH_Y_M_D,
    DateType.SLASH_M_D_H_M,
    DateType.SLASH_H_M_M_D,
    DateType.SLASH_M_D_H_M_S,
    DateType.POINT_Y_M_D_H_M,
    DateType.POINT_M_D,
    DateType.SHORT_H_M_S
)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class DateType {
    companion object {
        const val LINE_Y_M_D_H_M_S_S = "yyyy-MM-dd HH:mm:ss:SSS"
        const val LINE_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss"
        const val LINE_Y_M_D_H_M = "yyyy-MM-dd HH:mm"
        const val LINE_Y_M_D = "yyyy-MM-dd"
        const val LINE_M_D_H_M = "MM-dd HH:mm:ss"
        const val SLASH_Y_M_D_H_M_S = "yyyy/MM/dd HH:mm:ss"
        const val SLASH_Y_M_D_H_M = "yyyy/MM/dd HH:mm"
        const val SLASH_Y_M_D = "yyyy/MM/dd"
        const val SLASH_M_D_H_M = "MM/dd HH:mm"
        const val SLASH_H_M_M_D = "HH:mm MM/dd"
        const val SLASH_M_D_H_M_S = "MM/dd HH:mm:ss"
        const val POINT_Y_M_D_H_M = "yyyy.MM.dd HH:mm"
        const val POINT_M_D = "MM.dd"
        const val SHORT_H_M_S = "HH:mm:ss"


    }
}


/**
 * 将毫秒转化为 分钟：秒 的格式
 * @return
 */
fun Long.conversionCountDownTimeToHms(): String {
    val hours = (this / 1000 / 60 / 60).toInt()//小时
    val minute = (this / 1000 / 60 % 60).toInt()//分钟
    val second = (this / 1000 % 60).toInt()//秒数

    return StringBuffer().append(if (hours >= 10) hours else "0$hours")
        .append(":")
        .append(if (minute >= 10) minute else "0$minute")
        .append(":")
        .append(if (second >= 10) second else "0$second").toString()
}

/**
 * 获取当前时间
 *
 * @return
 */
fun getTime(): Long {
    return System.currentTimeMillis()
}

/**
 * 时间戳转化整数时间，比如小时、分、秒
 */
private fun Long.toTimeOfInt(spaceTime: Long) = (this / spaceTime).toInt()

/**
 * 时间搓转化为分钟，不带单位
 */
fun Long.toHour() = toTimeOfInt(MS_HOUR)

/**
 * 时间搓转化为分钟，带单位
 */
fun Long.toHour(unit: String?) = "${toHour()}$unit"

/**
 * 时间搓转化为分钟，不带单位
 */
fun Long.toMinute() = toTimeOfInt(MS_MINUTE)

/**
 * 时间搓转化为分钟，带单位
 */
fun Long.toMinute(unit: String?) = "${toMinute()}$unit"

/**
 * 时间搓转化为秒,不带单位
 */
fun Long.toSecond() = toTimeOfInt(MS_SECOND)

/**
 * 时间搓转化为秒,带单位
 */
fun Long.toSecond(unit: String?) = "${toSecond()}$unit"

/**
 * 时间搓转化为分秒，例如: 15分钟5秒,15m5s
 */
fun Long.toMinuteSecond(unitMinute: String, unitSecond: String): String {
    val minute = toMinute()//分钟
    val second = (this / MS_SECOND % 60).toInt()//秒数

    return StringBuffer()
        .append(minute)
        .append(unitMinute)
        .append(second)
        .append(unitSecond).toString()
}

/**
 * 时间搓转化为分秒，例如: 15:03
 */
fun Long?.toFormatMinuteSecond(unitMinute: String, unitSecond: String): String? {
    this?.let {
        val minute = toMinute()//分钟
        val second = (this / MS_SECOND % 60).toInt()//秒数

        return StringBuffer()
            .append(if (minute >= 10) minute else "0$minute")
            .append(unitMinute)
            .append(if (second >= 10) second else "0$second")
            .append(unitSecond).toString()
    }
    return null

}

/**
 * 时间搓转化为分秒，例如: 02:15:03  02小时05分钟05秒
 */
fun Long?.toFormatHourMinuteSecond(
    unitHour: String,
    unitMinute: String,
    unitSecond: String
): String? {
    this?.let {
        val hours = toHour()//小时
        val minute = (this / MS_MINUTE % 60).toInt()//分钟
        val second = (this / MS_SECOND % 60).toInt()//秒数

        return StringBuffer()
            .append(if (hours >= 10) hours else "0$hours")
            .append(unitHour)
            .append(if (minute >= 10) minute else "0$minute")
            .append(unitMinute)
            .append(if (second >= 10) second else "0$second")
            .append(unitSecond).toString()
    }
    return null
}

/**
 * 时间戳转换成字符窜
 */
fun Long?.toYMdDate(): String? {
    return toData(DateType.LINE_Y_M_D)
}

/**
 * 时间戳转换成字符窜
 */
fun Long?.toYMdHmDate(): String? {
    return toData(DateType.LINE_Y_M_D_H_M)
}

/**
 * 时间戳转换成字符窜
 */
fun Long?.toMdHmDate(): String? {
    return toData(DateType.SLASH_M_D_H_M)
}

/**
 * 时间戳转换成字符窜
 */
fun Long?.toYMdHmsDate(): String? {
    return toData(DateType.LINE_Y_M_D_H_M_S)
}

/**
 * 时间戳转换成字符窜
 */
fun Long?.toHmsData(): String? {
    return toData(DateType.SHORT_H_M_S)
}

fun Long?.toData(type: String): String? {
    this?.let {
        return SimpleDateFormat(type, Locale.getDefault()).format(this)
    }
    return null
}

fun String?.dateToLong(type: String? = DateType.LINE_Y_M_D_H_M_S): Long {
    try {
        return this?.let {
            SimpleDateFormat(type, Locale.getDefault()).parse(it)?.time
        } ?: 0L
    } catch (e: Exception) {
        ToolLog.printStackTrace(e)
    }
    return 0L
}
