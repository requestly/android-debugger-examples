package com.ricardaparicio.cryptodemo.core.di

import android.content.Context
import com.ricardaparicio.cryptodemo.core.networking.loggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.requestly.rqinterceptor.api.RQCollector
import io.requestly.rqinterceptor.api.RQInterceptor
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext appContext: Context): OkHttpClient {
        val collector = RQCollector(
            context = appContext,
            sdkKey = "bk9fvxFXM5HNwaMc6gkZ",
        )
        val rqInterceptor = RQInterceptor.Builder(appContext)
            .collector(collector)
            .build()

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(rqInterceptor)
            .build()
    }
}