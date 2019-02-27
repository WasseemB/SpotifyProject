package com.wasseemb.musicplayersample.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "firebase_tracks")
data class FirebaseTrack(val artist: String = "", val name: String = "",
    val genre: String = "", @PrimaryKey val uri: String = "", val albumImageUrl: String = "",
    val weight: Int = 0, var vote: Int = 0, val type: String = "")