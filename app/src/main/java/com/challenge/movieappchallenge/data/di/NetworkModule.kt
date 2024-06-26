package com.challenge.movieappchallenge.data.di

import com.challenge.movieappchallenge.App
import com.challenge.movieappchallenge.data.remote.RetrofitApi
import com.challenge.movieappchallenge.util.API_KEY
import com.challenge.movieappchallenge.util.BASE_URL
import com.challenge.movieappchallenge.util.NetworkState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideApp(): App {
        return App.instance
    }

    @Provides
    @Singleton
    fun getOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.apply {
            retryOnConnectionFailure(true)
            readTimeout(1, TimeUnit.MINUTES)
            connectTimeout(1, TimeUnit.MINUTES)
            writeTimeout(5, TimeUnit.MINUTES)
            addInterceptor { chain ->
                val original = chain.request()
                val http = original.url
                    .newBuilder().addQueryParameter("api_key", API_KEY).build()
                val newRequest = original.newBuilder().url(http).build()
                return@addInterceptor chain.proceed(newRequest)
            }
            addInterceptor(logging)
        }.build()
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun getMoshi(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    @Provides
    @Singleton
    fun getRetrofit(okHttpClient: OkHttpClient, moshi: MoshiConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(moshi)
            .build()
    }

    @Provides
    @Singleton
    fun provideAPIService(
        retrofit: Retrofit,
    ): RetrofitApi {
        return retrofit.create(RetrofitApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkState(): NetworkState {
        return NetworkState
    }

}
