package com.riadsafowan.to_do.di

import com.riadsafowan.to_do.data.local.pref.UserDataStore
import com.riadsafowan.to_do.data.remote.ApiClient
import com.riadsafowan.to_do.data.remote.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(userDataStore: UserDataStore): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .addInterceptor(AuthInterceptor(userDataStore))
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api-dev.dealbd.com/catalog/api/v1/public/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): ApiClient = retrofit.create(ApiClient::class.java)
}