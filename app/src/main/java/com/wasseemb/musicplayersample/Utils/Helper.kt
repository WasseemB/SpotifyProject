package com.wasseemb.musicplayersample.Utils

import com.wasseemb.musicplayersample.vo.FirebaseTrack
import com.wasseemb.musicplayersample.vo.RecentlyPlayed.Item
import com.wasseemb.musicplayersample.vo.Tracks

class Helper {
  fun createFirebaseTrackMap(trackList: List<Tracks.Item>): HashMap<String, FirebaseTrack> {
    val songHashMap = HashMap<String, FirebaseTrack>()
    trackList.forEach { item ->
      songHashMap[item.track.uri] = FirebaseTrack(
          artist = item.track.artists[0].name, name = item.track.name,
          uri = item.track.uri,
          albumImageUrl = item.track.album.images[0].url)
    }
    return songHashMap
  }

  @JvmName("createFirebaseTrackMapForRecent")
  fun createFirebaseTrackMap(trackList: List<Item>): HashMap<String, FirebaseTrack> {
    val songHashMap = HashMap<String, FirebaseTrack>()
    trackList.forEach { item ->
      songHashMap[item.track.uri] = FirebaseTrack(
          artist = item.track.artists[0].name, name = item.track.name,
          uri = item.track.uri,
          albumImageUrl = "")
    }
    return songHashMap
  }

  fun hashMapFromFirebaseTrack(tracks: List<FirebaseTrack>): HashMap<String, FirebaseTrack> {
    val songHashMap = HashMap<String, FirebaseTrack>()
    tracks.forEach { songHashMap[it.uri] = it }
    return songHashMap
  }
}