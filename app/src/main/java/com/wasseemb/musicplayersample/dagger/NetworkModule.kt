package com.wasseemb.musicplayersample.dagger

import com.wasseemb.musicplayersample.api.HeaderInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


@Module(includes = [HeaderInterceptorModule::class])
class NetworkModule {


  @Provides
  @ApplicationScope
  fun httpLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
  }

  @Provides
  @ApplicationScope
  fun okHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    //httpClient.addInterceptor(logging)
    httpClient.addInterceptor(headerInterceptor)
    return httpClient.build()
  }
}