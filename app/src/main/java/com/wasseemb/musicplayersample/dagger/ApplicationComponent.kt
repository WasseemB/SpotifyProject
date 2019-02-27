package com.wasseemb.musicplayersample.dagger

import androidx.lifecycle.ViewModelProvider
import com.wasseemb.musicplayersample.fragments.RecentlyPlayedFragment
import com.wasseemb.musicplayersample.fragments.TrackFragment
import com.wasseemb.musicplayersample.NavigationActivity
import com.wasseemb.musicplayersample.SpotifyRepository
import com.wasseemb.musicplayersample.dagger2.SpotifyViewModelFactoryModule
import dagger.Component


@ApplicationScope
@Component(
    modules = [ApplicationModule::class,
      SpotifyViewModelFactoryModule::class])
interface ApplicationComponent {


  fun inject(trackFragment: TrackFragment)
  fun inject(recentlyPlayedFragment: RecentlyPlayedFragment)

  fun inject(navigationActivity: NavigationActivity)

  //fun getSpotifyService(): SpotifyApiService

  fun getSpotifyRepository(): SpotifyRepository

  // fun getFirebaseDao(): FirebaseTrackDao

  fun getViewModelFactory(): ViewModelProvider.Factory
}