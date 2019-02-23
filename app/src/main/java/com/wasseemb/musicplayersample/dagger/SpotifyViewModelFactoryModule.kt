package com.wasseemb.musicplayersample.dagger2

import androidx.lifecycle.ViewModelProvider
import com.wasseemb.musicplayersample.SpotifyViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class SpotifyViewModelFactoryModule {
  @Binds
  abstract fun bindViewModelFactory(
      viewModelFactory: SpotifyViewModelFactory): ViewModelProvider.Factory
}