package com.ricardaparicio.cryptodemo.core.networking

import com.orhanobut.logger.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

private const val LOGGING_TAG = "ApiLog"

val loggingInterceptor
    get() =
        HttpLoggingInterceptor { message ->
            Timber.tag(LOGGING_TAG).d(message)
        }.apply {
            level = when (BuildConfig.DEBUG) {
                true -> HttpLoggingInterceptor.Level.BODY
                false -> HttpLoggingInterceptor.Level.NONE
            }
        }

