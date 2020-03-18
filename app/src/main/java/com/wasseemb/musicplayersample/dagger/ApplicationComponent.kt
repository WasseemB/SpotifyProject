package com.wasseemb.musicplayersample.dagger

import com.wasseemb.musicplayersample.BottomNavigationActivity
import com.wasseemb.musicplayersample.dagger2.SpotifyViewModelFactoryModule
import com.wasseemb.musicplayersample.fragments.QueueTrackFragment
import com.wasseemb.musicplayersample.fragments.RecentlyPlayedFragment
import com.wasseemb.musicplayersample.fragments.SettingsFragment
import dagger.Component


@ApplicationScope
@Component(modules = [ApplicationModule::class, SpotifyViewModelFactoryModule::class])
interface ApplicationComponent {
  fun inject(recentlyPlayedFragment: RecentlyPlayedFragment)
  fun inject(queueTrackFragment: QueueTrackFragment)
  fun inject(settingsFragment: SettingsFragment)
  fun inject(bottomNavigationActivity: BottomNavigationActivity)
}