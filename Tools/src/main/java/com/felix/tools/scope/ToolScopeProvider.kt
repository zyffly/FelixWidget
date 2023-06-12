package com.felix.tools.scope

import android.util.ArrayMap
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext

/**
 * Created by zyf on 2023-05-18 18:13
 * Describe: 根据viewModel携程来实现的
 */

open class ToolScopeProvider {
    companion object {
        private const val JOB_KEY = "com.felix.tools.scope.JOB_KEY"
    }

    private val mBagOfTags by lazy { ArrayMap<String, CloseableCoroutineScope>() }
    private var mIsClosed = AtomicBoolean(false)

    /**
     * [CoroutineScope] tied to this [ViewModel].
     * This scope will be canceled when ViewModel will be cleared, i.e [ViewModel.onCleared] is called
     *
     * This scope is bound to
     * [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate]
     */
    val scope: CoroutineScope
        get() {
            val scope: CoroutineScope? = this.getTag(JOB_KEY)
            if (scope != null) {
                return scope
            }
            return setTagIfAbsent(
                JOB_KEY,
                CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
            )
        }


    @MainThread
    fun clear() {
        // Since clear() is final, this method is still called on mock objects
        // and in those cases, mBagOfTags is null. It'll always be empty though
        // because setTagIfAbsent and getTag are not final so we can skip
        // clearing it
        synchronized(mBagOfTags) {
            for (value in mBagOfTags.values) {
                // see comment for the similar call in setTagIfAbsent
                closeWithRuntimeException(value)
            }
        }
        mIsClosed.set(true)
        mBagOfTags.clear()
    }

    /**
     * Sets a tag associated with this viewmodel and a key.
     * If the given `newValue` is [Closeable],
     * it will be closed once [.clear].
     *
     *
     * If a value was already set for the given key, this calls do nothing and
     * returns currently associated value, the given `newValue` would be ignored
     *
     *
     * If the ViewModel was already cleared then close() would be called on the returned object if
     * it implements [Closeable]. The same object may receive multiple close calls, so method
     * should be idempotent.
     */
    fun setTagIfAbsent(key: String, newValue: CloseableCoroutineScope): CoroutineScope {
        var previous = newValue

        synchronized(mBagOfTags) {
            mBagOfTags[key]?.let {
                previous = it
            } ?: {
                mBagOfTags[key] = newValue
            }
        }
        if (mIsClosed.get()) {
            // It is possible that we'll call close() multiple times on the same object, but
            // Closeable interface requires close method to be idempotent:
            // "if the stream is already closed then invoking this method has no effect." (c)
            closeWithRuntimeException(previous)
        }
        return previous
    }

    /**
     * Returns the tag associated with this viewmodel and the specified key.
     */
    fun getTag(key: String?): CoroutineScope? {
        synchronized(mBagOfTags) {
            mBagOfTags[key]?.let { value ->
                return value
            }
            return null
        }
    }

    private fun closeWithRuntimeException(obj: CloseableCoroutineScope) {
        try {
            obj.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    inner class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
        override val coroutineContext: CoroutineContext = context

        override fun close() {
            coroutineContext.cancel()
        }
    }
}