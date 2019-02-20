package com.wasseemb.musicplayersample

import androidx.lifecycle.LiveData
import com.wasseemb.musicplayersample.Database.FirebaseTrackDao
import com.wasseemb.musicplayersample.api.SpotifyApiService
import com.wasseemb.musicplayersample.vo.FirebaseTrack
import com.wasseemb.musicplayersample.vo.RecentlyPlayed
import com.wasseemb.musicplayersample.vo.Tracks
import com.wasseemb.musicplayersample.vo.UserResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SpotifyRepository(private val spotifyApiService: SpotifyApiService,
    private val firebaseTrackDao: FirebaseTrackDao) {

  fun getTracks(): Observable<Tracks> {
    return spotifyApiService.getUserTracks().observeOn(AndroidSchedulers.mainThread()).subscribeOn(
        Schedulers.io())
  }


  fun getRemoteTracks() {
    spotifyApiService.getUserTracks().observeOn(
        Schedulers.computation())
        .subscribeOn(Schedulers.io())
        //Retain orignal value of the stream before sideeffects
        .flatMap { tracks ->
          Observable.fromIterable(tracks.items)
        }
        .subscribe { result ->
          firebaseTrackDao.insert(FirebaseTrack(
              artist = result.track.artists[0].name, name = result.track.name,
              uri = result.track.uri,
              albumImageUrl = result.track.album.images[0].url))
        }
  }

  fun getSpotifyTracks(): LiveData<List<FirebaseTrack>> = firebaseTrackDao.all


  fun getRecentlyPlayed(): Single<RecentlyPlayed> {
    return spotifyApiService.getRecentlyPlayed().observeOn(
        AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
  }

  fun getUserData(): Single<UserResponse> {
    return spotifyApiService.getUserData().observeOn(
        AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
  }

}



