package com.challenge.movieappchallenge.util

import android.util.Log

object Logger {
    var isEnabled = false

    @JvmStatic
    fun e(tag: String, message: String) {
        if (isEnabled) {
            Log.e(tag, message)
        }
    }

    @JvmStatic
    fun v(tag: String, message: String) {
        if (isEnabled) {
            Log.v(tag, message)
        }
    }

    @JvmStatic
    fun d(tag: String, message: String) {
        if (isEnabled) {
            Log.d(tag, message)
        }
    }

    @JvmStatic
    fun i(tag: String, message: String) {
        if (isEnabled) {
            Log.i(tag, message)
        }
    }
}

