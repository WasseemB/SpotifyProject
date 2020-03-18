package com.wasseemb.musicplayersample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wasseemb.musicplayersample.vo.FirebaseTrack
import javax.inject.Inject

class SpotifyViewModel @Inject constructor(
    private val spotifyRepository: SpotifyRepository) : ViewModel() {
  var token = MutableLiveData<String>()


  var displayableTrack: LiveData<List<FirebaseTrack>> = spotifyRepository.getSpotifyTracks()
  var displayableRecent: LiveData<List<FirebaseTrack>> = spotifyRepository.getSpotifyRecents()
  var displayableAll: LiveData<List<FirebaseTrack>> = spotifyRepository.getSpotifyAll()


  fun getLocalTracks(): LiveData<List<FirebaseTrack>> = displayableTrack
  fun getLocalRecents(): LiveData<List<FirebaseTrack>> = displayableRecent
  fun getLocal(): LiveData<List<FirebaseTrack>> = displayableAll


  fun loadTracks() {
    spotifyRepository.getRemoteTracks()
  }

  fun loadRecents() {
    spotifyRepository.getRemoteRecentlyPlayed()
  }


}