package com.felix.tools

import android.view.View
import android.view.ViewTreeObserver
import java.lang.reflect.ParameterizedType

/**
 * Created by zyf on 2023-06-09 10:45
 * Describe:
 */
inline fun <T> ifElseRt(data: T?, booTrue: (T) -> T, booFalse: () -> T) =
    if (null != data) {
        booTrue(data)
    } else {
        booFalse()
    }


inline fun <T> ifElse(data: T?, booTrue: (T) -> Unit, noinline booFalse: (() -> Unit)? = null) =
    if (null != data) {
        booTrue(data)
    } else {
        booFalse?.invoke()
    }

/**
 * For循环时，达到某种条件，跳出否循环，并返回值
 */
inline fun <T> Iterable<T>.breakForEach(booTrue: (T) -> Boolean) {
    kotlin.run run@{
        forEach {
            if (booTrue(it)) {
                return@run
            }
        }
    }
}

/**
 * For循环时，达到某种条件，跳出否循环，并返回值
 */
inline fun <T> Array<T>.breakForEach(booTrue: (T) -> Boolean) {
    kotlin.run run@{
        forEach {
            if (booTrue(it)) {
                return@run
            }
        }
    }
}

/**
 * For循环时，达到某种条件，跳出否循环，并返回值
 */
inline fun <T> Collection<T>.breakForEach(booTrue: (T) -> Boolean) {
    kotlin.run run@{
        forEach {
            if (booTrue(it)) {
                return@run
            }
        }
    }
}

/**
 * For循环时，达到某种条件，跳出否循环，并返回下标。循环完后，还没有达到触发条件，则继续
 */
inline fun <T> Iterable<T>.breakForEachIndexed(booTrue: (index: Int, T) -> Boolean) {
    kotlin.run run@{
        forEachIndexed { index, t ->
            if (booTrue(index, t)) {
                return@run
            }
        }
    }
}

/**
 * For循环时，达到某种条件，跳出否循环，并返回下标。循环完后，还没有达到触发条件，则继续
 */
inline fun <T> Collection<T>.breakForEachIndexed(booTrue: (index: Int, T) -> Boolean) {
    kotlin.run run@{
        forEachIndexed { index, t ->
            if (booTrue(index, t)) {
                return@run
            }
        }
    }
}

/**
 * 扩展函数,监听view的绘制完成，获取view高度用的
 */
inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

/**
 * 获取泛型中的类型
 */
@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}