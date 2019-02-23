package com.wasseemb.musicplayersample.dagger2

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule(val context: Context) {

  @Provides
  fun context(): Context {
    return context
  }

}