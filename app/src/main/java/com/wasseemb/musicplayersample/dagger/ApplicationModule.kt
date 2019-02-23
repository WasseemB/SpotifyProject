package com.wasseemb.musicplayersample.dagger

import android.content.Context
import androidx.room.Room
import com.wasseemb.musicplayersample.Database.FirebaseTrackDao
import com.wasseemb.musicplayersample.Database.FirebaseTracksDatabase
import com.wasseemb.musicplayersample.SpotifyRepository
import com.wasseemb.musicplayersample.api.SpotifyApiService
import dagger.Module
import dagger.Provides


@Module
class ApplicationModule {
  @Provides
  fun provideDb(context: Context): FirebaseTracksDatabase {
    return Room
        .databaseBuilder(context, FirebaseTracksDatabase::class.java, "firebasetracksdb.db")
        .fallbackToDestructiveMigration()
        .build()
  }

  @Provides
  fun spotifyRepository(spotifyApiService: SpotifyApiService,
      firebaseTrackDao: FirebaseTrackDao): SpotifyRepository {
    return SpotifyRepository(spotifyApiService,
        firebaseTrackDao)
  }

  @Provides
  fun provideFirebaseDao(firebaseTrackDatabase: FirebaseTracksDatabase): FirebaseTrackDao {
    return firebaseTrackDatabase.firebaseDao()
  }
}