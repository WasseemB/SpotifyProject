package com.wasseemb.musicplayersample.dagger2

import android.content.Context
import com.wasseemb.musicplayersample.dagger.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ContextModule(val context: Context) {

  @Provides
  @ApplicationScope
  fun context(): Context {
    return context
  }

}