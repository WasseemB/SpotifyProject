package com.wasseemb.musicplayersample.vo

data class FirebaseTrack(val artist: String = "", val name: String = "", val genre: String = "",
    val uri: String = "", val albumImageUrl: String = "", val weight: Int = 0, var vote: Int = 0)