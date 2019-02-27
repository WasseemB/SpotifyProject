package com.wasseemb.musicplayersample.dagger

import android.content.Context
import androidx.room.Room
import com.wasseemb.musicplayersample.database.FirebaseTrackDao
import com.wasseemb.musicplayersample.database.FirebaseTracksDatabase
import com.wasseemb.musicplayersample.SpotifyRepository
import com.wasseemb.musicplayersample.api.SpotifyApiService
import com.wasseemb.musicplayersample.dagger2.ContextModule
import dagger.Module
import dagger.Provides


@Module(includes = [ContextModule::class, SpotifyServiceModule::class])
class ApplicationModule {
  @Provides
  @ApplicationScope
  fun provideDb(context: Context): FirebaseTracksDatabase {
    return Room
        .databaseBuilder(context, FirebaseTracksDatabase::class.java, "firebasetracksdb.db")
        .fallbackToDestructiveMigration()
        .build()
  }

  @Provides
  @ApplicationScope
  fun spotifyRepository(spotifyApiService: SpotifyApiService,
      firebaseTrackDao: FirebaseTrackDao): SpotifyRepository {
    return SpotifyRepository(spotifyApiService,
        firebaseTrackDao)
  }

  @Provides
  @ApplicationScope
  fun provideFirebaseDao(firebaseTrackDatabase: FirebaseTracksDatabase): FirebaseTrackDao {
    return firebaseTrackDatabase.firebaseDao()
  }
}