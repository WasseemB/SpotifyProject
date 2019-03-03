package com.wasseemb.musicplayersample.dagger

import android.content.Context
import androidx.room.Room
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wasseemb.musicplayersample.SpotifyRepository
import com.wasseemb.musicplayersample.api.SpotifyApiService
import com.wasseemb.musicplayersample.dagger2.ContextModule
import com.wasseemb.musicplayersample.database.FirebaseTrackDao
import com.wasseemb.musicplayersample.database.FirebaseTracksDatabase
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


  @Provides
  @ApplicationScope
  fun provideFirebaseDatabaseReference(): DatabaseReference {
    return FirebaseDatabase.getInstance().reference
  }

}