package com.wasseemb.musicplayersample.dagger2

import androidx.lifecycle.ViewModel
import com.wasseemb.musicplayersample.SpotifyViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MyViewModelModule {
  @Binds
  @IntoMap
  @ViewModelKey(SpotifyViewModel::class)
  abstract fun bindMyViewModel(myViewModel: SpotifyViewModel): ViewModel
}