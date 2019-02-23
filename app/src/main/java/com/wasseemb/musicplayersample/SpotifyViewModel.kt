package com.wasseemb.musicplayersample

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wasseemb.musicplayersample.vo.FirebaseTrack
import javax.inject.Inject

class SpotifyViewModel @Inject constructor(
    private val spotifyRepository: SpotifyRepository) : ViewModel() {

  var displayableTrack: LiveData<List<FirebaseTrack>>


  init {
    displayableTrack = spotifyRepository.getSpotifyTracks()
  }

  fun getLocalTracks(): LiveData<List<FirebaseTrack>> = displayableTrack


  fun loadTracks() {
    spotifyRepository.getRemoteTracks()
  }


}