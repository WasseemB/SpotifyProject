package com.wasseemb.musicplayersample

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import com.wasseemb.musicplayersample.api.SpotifyApiService
import com.wasseemb.musicplayersample.database.FirebaseTrackDao
import com.wasseemb.musicplayersample.vo.FirebaseTrack
import com.wasseemb.musicplayersample.vo.RecentlyPlayed
import com.wasseemb.musicplayersample.vo.UserResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SpotifyRepository(private val spotifyApiService: SpotifyApiService,
    private val firebaseTrackDao: FirebaseTrackDao) {

  fun getSpotifyTracks(): LiveData<List<FirebaseTrack>> = firebaseTrackDao.track
  fun getSpotifyRecents(): LiveData<List<FirebaseTrack>> = firebaseTrackDao.recent
  fun getSpotifyAll(): LiveData<List<FirebaseTrack>> = firebaseTrackDao.all

  @SuppressLint("CheckResult")
  fun getRemoteTracks() {
    spotifyApiService.getUserTracks().observeOn(
        Schedulers.computation())
        .subscribeOn(Schedulers.io())
        .toObservable()
        //Retain orignal value of the stream before sideeffects
        .flatMap { tracks ->
          Observable.fromIterable(tracks.items).map { result ->
            FirebaseTrack(
                artist = result.track.artists[0].name, name = result.track.name,
                uri = result.track.uri,
                albumImageUrl = result.track.album.images[0].url, type = "normal")
          }
              .toList()
              .toObservable()
        }
        .subscribe { result ->
          firebaseTrackDao.insert(result)
        }
  }

  @SuppressLint("CheckResult")
  fun getRemoteRecentlyPlayed() {
    spotifyApiService.getRecentlyPlayed().observeOn(
        Schedulers.computation())
        .subscribeOn(Schedulers.io())
        .toObservable()
        //Retain orignal value of the stream before sideeffects
        .flatMap { tracks ->
          Observable.fromIterable(tracks.items).map { result ->
            FirebaseTrack(
                artist = result.track.artists[0].name, name = result.track.name,
                uri = result.track.uri,
                albumImageUrl = "", type = "recent")
          }
              .toList()
              .toObservable()
        }
        .subscribe { result ->
          firebaseTrackDao.insert(result)
        }
  }

  fun getRemoteRecentlyPlayed1() {
    spotifyApiService.getRecentlyPlayed()
        .observeOn(Schedulers.computation())
        .subscribeOn(Schedulers.io())
        .toObservable()
        .flatMapIterable { recentlyPlayed: RecentlyPlayed -> recentlyPlayed.items }
        .subscribe {
          spotifyApiService.getTrack(it.track.id)
              .observeOn(Schedulers.computation())
              .subscribeOn(Schedulers.io())
              .subscribe {
                firebaseTrackDao.insert(
                    FirebaseTrack(artist = it.artists[0].name, name = it.name, uri = it.uri,
                        albumImageUrl = it.album.images[0].url, type = "recent"))
              }


        }
  }


  fun getUserData(): Single<UserResponse> {
    return spotifyApiService.getUserData().observeOn(
        AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
  }

}



