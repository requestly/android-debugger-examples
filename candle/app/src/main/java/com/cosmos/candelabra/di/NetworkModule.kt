package com.cosmos.candelabra.di

import android.content.Context
import com.cosmos.candelabra.CandelabraApplication
import com.cosmos.candelabra.data.remote.api.autoc.AutocApi
import com.cosmos.candelabra.data.remote.api.yahoofinance.ChartPeriodConverterFactory
import com.cosmos.candelabra.data.remote.api.yahoofinance.YahooFinanceApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.requestly.rqinterceptor.api.RQCollector
import io.requestly.rqinterceptor.api.RQInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideYahooMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext appContext: Context): OkHttpClient {
        val collector = RQCollector(
            context = appContext,
            sdkKey = "2C9TkGJXvAs5o04B86Py",
        )
        val rqInterceptor = RQInterceptor.Builder(appContext)
            .collector(collector)
            .build()
        return OkHttpClient.Builder().addInterceptor(rqInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideYahooFinanceApi(moshi: Moshi, okHttpClient: OkHttpClient): YahooFinanceApi {
        return Retrofit.Builder()
            .baseUrl(YahooFinanceApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addConverterFactory(ChartPeriodConverterFactory())
            .client(okHttpClient)
            .build()
            .create(YahooFinanceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAutocApi(moshi: Moshi, okHttpClient: OkHttpClient): AutocApi {
        return Retrofit.Builder()
            .baseUrl(AutocApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(AutocApi::class.java)
    }
}
