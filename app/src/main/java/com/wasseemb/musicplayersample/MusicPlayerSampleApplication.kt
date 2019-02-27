package com.wasseemb.musicplayersample

import android.app.Application
import com.wasseemb.musicplayersample.api.HeaderInterceptor
import com.wasseemb.musicplayersample.dagger.ApplicationComponent
import com.wasseemb.musicplayersample.dagger.DaggerApplicationComponent
import com.wasseemb.musicplayersample.dagger.HeaderInterceptorModule
import com.wasseemb.musicplayersample.dagger2.ContextModule


class MusicPlayerSampleApplication : Application() {


  lateinit var component: ApplicationComponent

  override fun onCreate() {
    super.onCreate()
    component = DaggerApplicationComponent.builder().contextModule(
        ContextModule(this)).headerInterceptorModule(
        HeaderInterceptorModule(HeaderInterceptor(""))).build()

  }


}