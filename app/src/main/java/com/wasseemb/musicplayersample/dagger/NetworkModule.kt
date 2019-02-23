package com.wasseemb.musicplayersample.dagger

import com.wasseemb.musicplayersample.api.HeaderInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


@Module
class NetworkModule {


  @Provides
  fun httpLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
  }

//  @Provides
//  fun headerInterceptor(token: String): Interceptor {
//    val interceptor = Interceptor { chain ->
//      chain.run {
//        proceed(request().newBuilder()
//            .addHeader("Authorization", "Bearer " + token)
//            .build())
//      }
//    }
//    return interceptor
//
//  }

  @Provides
  fun okHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    //httpClient.addInterceptor(logging)
    httpClient.addInterceptor(headerInterceptor)
    return httpClient.build()
  }
}