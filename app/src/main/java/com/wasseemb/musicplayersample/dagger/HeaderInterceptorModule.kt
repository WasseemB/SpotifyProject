package com.wasseemb.musicplayersample.dagger

import com.wasseemb.musicplayersample.api.HeaderInterceptor
import dagger.Module
import dagger.Provides

@Module
class HeaderInterceptorModule(val interceptor: HeaderInterceptor) {

  @Provides
  fun headerInterceptor(): HeaderInterceptor {
    return interceptor
  }
}