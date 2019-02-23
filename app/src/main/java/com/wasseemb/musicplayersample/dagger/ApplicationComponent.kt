package com.wasseemb.musicplayersample.dagger

import androidx.lifecycle.ViewModelProvider
import com.wasseemb.musicplayersample.Database.FirebaseTrackDao
import com.wasseemb.musicplayersample.SpotifyRepository
import com.wasseemb.musicplayersample.api.SpotifyApiService
import com.wasseemb.musicplayersample.dagger2.ContextModule
import com.wasseemb.musicplayersample.dagger2.SpotifyViewModelFactoryModule
import dagger.Component


@Component(
    modules = [SpotifyServiceModule::class, ApplicationModule::class, ContextModule::class,
      NetworkModule::class, HeaderInterceptorModule::class, SpotifyViewModelFactoryModule::class])
interface ApplicationComponent {

  fun getSpotifyService(): SpotifyApiService

  fun getSpotifyRepository(): SpotifyRepository

  fun getFirebaseDao(): FirebaseTrackDao

  fun getViewModelFactory(): ViewModelProvider.Factory
}