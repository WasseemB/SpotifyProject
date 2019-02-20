package com.wasseemb.musicplayersample.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wasseemb.musicplayersample.vo.FirebaseTrack


@Database(entities = [FirebaseTrack::class], version = 1)
abstract class FirebaseTracksDatabase : RoomDatabase() {
  abstract fun firebaseDao(): FirebaseTrackDao

}