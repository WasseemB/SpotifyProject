package com.wasseemb.musicplayersample.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wasseemb.musicplayersample.vo.FirebaseTrack

@Dao
interface FirebaseTrackDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(firebaseTrack: FirebaseTrack)

  @Update
  fun update(firebaseTrack: FirebaseTrack)


  @get:Query("SELECT * FROM firebase_tracks WHERE type ='normal'")
  val all: LiveData<List<FirebaseTrack>>

  @get:Query("SELECT * FROM firebase_tracks WHERE type='recent'")
  val recent: LiveData<List<FirebaseTrack>>
}