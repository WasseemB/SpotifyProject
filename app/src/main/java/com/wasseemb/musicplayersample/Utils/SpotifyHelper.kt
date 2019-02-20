package com.wasseemb.musicplayersample.Utils

import android.app.Activity
import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.wasseemb.musicplayersample.vo.RecentlyPlayed

class SpotifyHelper {
  fun createAuthRequest(activty: Activity) {
    val builder = AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN,
        REDIRECT_URI)

    builder.setScopes(
        arrayOf("user-library-read", "user-read-private", "user-read-email", "streaming",
            "playlist-read-private", "user-top-read", "app-remote-control",
            "user-modify-playback-state", "playlist-modify-public", "playlist-modify-private",
            "user-read-recently-played"))
    val request = builder.build()

    AuthenticationClient.openLoginActivity(activty,
        REQUEST_CODE,
        request)
  }


  fun playTrack(item: RecentlyPlayed.Item, context: Context?): SpotifyAppRemote? {
    var spotifyAppRemoter: SpotifyAppRemote? = null
    val connectionParams = ConnectionParams.Builder(CLIENT_ID)
        .setRedirectUri(REDIRECT_URI)
        .showAuthView(true)
        .build()
    SpotifyAppRemote.connect(context, connectionParams,
        object : Connector.ConnectionListener {

          override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
            Log.d("MainActivity", "Connected! Yay!")
            spotifyAppRemoter = spotifyAppRemote

            // Now you can start interacting with App Remote
            connected(item, spotifyAppRemote)
          }

          override fun onFailure(throwable: Throwable) {
            Log.e("MainActivity", throwable.message, throwable)

            // Something went wrong when attempting to connect! Handle errors here
          }
        })
    return spotifyAppRemoter
  }

  private fun connected(item: RecentlyPlayed.Item, spotifyAppRemote: SpotifyAppRemote?) {
    // Play a playlist
    spotifyAppRemote?.playerApi?.play(item.track.uri)


    // Subscribe to PlayerState
    spotifyAppRemote?.playerApi
        ?.subscribeToPlayerState()
        ?.setEventCallback { playerState ->
          val track = playerState.track
          if (track != null) {
            Log.d("MainActivity", track.name + " by " + track.artist.name)
          }
        }
  }

}