package com.felix.tools

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * IO中执行某些任务，结果在UI主线线程中获取,有返回值
 */
inline fun <T> CoroutineScope.launchAsyncIOWithMain(
    crossinline async: () -> T,
    crossinline main: (T) -> Unit
) {
    launchAsync(Dispatchers.IO, async, Dispatchers.Main, main)
}

/**
 * Default中执行某些任务，结果在UI主线线程中获取,有返回值
 */
inline fun <T> CoroutineScope.launchAsyncWithMain(
    crossinline async: () -> T,
    crossinline main: (T) -> Unit
) {
    launch {
        val obj = async(Dispatchers.Default) {
            async()
        }
        withContext(Dispatchers.Main) {
            main(obj.await())
        }
    }
}

/**
 * 指定线程中执行某些任务，结果在指定线程中获取结果,有返回值
 */
inline fun <T> CoroutineScope.launchAsync(
    dispatchers: CoroutineContext,
    crossinline async: () -> T,
    withDispatchers: CoroutineContext,
    crossinline main: (T) -> Unit
) {
    launch {
        val obj = async(dispatchers) {
            async()
        }
        withContext(withDispatchers) {
            main(obj.await())
        }
    }
}

/**
 * IO中执行某些任务，结果在UI主线线程中获取
 */
inline fun CoroutineScope.launchIOWithMain(
    crossinline launch: () -> Unit,
    crossinline main: () -> Unit
) {
    launch(Dispatchers.IO, launch, Dispatchers.Main, main)
}

/**
 * IO中执行某些任务，结果在UI主线线程中获取
 */
inline fun <T> CoroutineScope.launchIOWithMain(
    crossinline launch: () -> T,
    crossinline main: (T) -> Unit
) {
    launch(Dispatchers.IO, launch, Dispatchers.Main, main)
}

/**
 * Default中执行某些任务，结果在UI主线线程中获取
 */
inline fun CoroutineScope.launchWithMain(
    crossinline launch: () -> Unit,
    crossinline main: () -> Unit
) {
    launch(Dispatchers.Default, launch, Dispatchers.Main, main)
}

/**
 * Default中执行某些任务，结果在UI主线线程中获取
 */
inline fun <T> CoroutineScope.launchWithMain(
    crossinline launch: () -> T,
    crossinline main: (T) -> Unit
) {
    launch(Dispatchers.Default, launch, Dispatchers.Main, main)
}

/**
 * 指定线程中执行某些任务，结果在指定线程中获取结果
 */
inline fun CoroutineScope.launch(
    dispatchers: CoroutineContext,
    crossinline launch: () -> Unit,
    withDispatchers: CoroutineContext,
    crossinline main: () -> Unit
) {
    launch(dispatchers) {
        launch()
        withContext(withDispatchers) { main() }
    }
}

/**
 * 指定线程中执行某些任务，结果在指定线程中获取结果
 */
inline fun <T> CoroutineScope.launch(
    dispatchers: CoroutineContext,
    crossinline launch: () -> T,
    withDispatchers: CoroutineContext,
    crossinline main: (T) -> Unit
) {
    launch(dispatchers) {
        val t = launch()
        withContext(withDispatchers) { main(t) }
    }
}


/**
 * Default线程中延迟执行某些函数
 */
inline fun CoroutineScope.delayDefault(delayTime: Long, crossinline run: () -> Unit) {
    delayRun(Dispatchers.Default, delayTime, run)
}

/**
 * IO线程中延迟执行某些函数
 */
inline fun CoroutineScope.delayIO(delayTime: Long, crossinline run: () -> Unit) {
    delayRun(Dispatchers.IO, delayTime, run)
}

/**
 * Main线程中延迟执行某些函数
 */
inline fun CoroutineScope.delayMain(delayTime: Long, crossinline run: () -> Unit) {
    delayRun(Dispatchers.Main, delayTime, run)
}

/**
 * 指定线程中延迟执行某些函数
 */
inline fun CoroutineScope.delayRun(
    dispatchers: CoroutineContext, delayTime: Long,
    crossinline run: () -> Unit
) {
    launch {
        delay(delayTime)
        withContext(dispatchers) { run() }
    }
}

/**
 * Default线程中执行某些函数
 */
inline fun CoroutineScope.postDefault(crossinline run: () -> Unit) {
    postRun(Dispatchers.Default, run)
}

/**
 * IO线程中执行某些函数
 */
inline fun CoroutineScope.postIO(crossinline run: () -> Unit) {
    postRun(Dispatchers.IO, run)
}

/**
 * Main线程中执行某些函数
 */
inline fun CoroutineScope.postMain(crossinline run: () -> Unit) {
    postRun(Dispatchers.Main, run)
}

/**
 * 指定线程中执行某些函数
 */
inline fun CoroutineScope.postRun(dispatchers: CoroutineContext, crossinline run: () -> Unit) {
    launch(dispatchers) { run() }
}