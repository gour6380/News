package com.gourav.news.utils

import android.os.Handler
import android.os.Looper

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors

private constructor(val diskIO: Executor, private val mainThread: Executor) {

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    companion object {

        private val LOCK = Any()
        private var sInstance: AppExecutors? = null


        val instance: AppExecutors
            get() {
                if (sInstance == null) {
                    synchronized(LOCK) {
                        sInstance = AppExecutors(
                                Executors.newSingleThreadExecutor(),
                                MainThreadExecutor()
                        )
                    }
                }
                return sInstance!!
            }
    }
}
