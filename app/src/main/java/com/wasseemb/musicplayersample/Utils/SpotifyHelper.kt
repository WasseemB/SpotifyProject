package com.wasseemb.musicplayersample.Utils

import android.app.Activity
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse

class SpotifyHelper {
  fun createAuthRequest(activty: Activity) {
    val builder = AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN,
        REDIRECT_URI)

    builder.setScopes(
        arrayOf("user-library-read", "user-read-private", "user-read-email", "streaming",
            "playlist-read-private", "user-top-read", "app-remote-control",
            "user-modify-playback-state", "playlist-modify-public", "playlist-modify-private","user-read-recently-played"))
    val request = builder.build()

    AuthenticationClient.openLoginActivity(activty,
        REQUEST_CODE,
        request)
  }

}