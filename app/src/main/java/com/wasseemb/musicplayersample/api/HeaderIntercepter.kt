package com.wasseemb.musicplayersample.api

import okhttp3.Interceptor
import okhttp3.Response


class HeaderInterceptor(val token: String) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response = chain.run {
    proceed(
        request()
            .newBuilder()
            .addHeader("Authorization", "Bearer " + token)
            .build()
    )
  }
}