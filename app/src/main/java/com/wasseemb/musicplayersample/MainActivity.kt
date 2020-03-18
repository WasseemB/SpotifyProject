package com.wasseemb.musicplayersample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.wasseemb.musicplayersample.extensions.PreferenceHelper
import com.wasseemb.musicplayersample.utils.REQUEST_CODE
import com.wasseemb.musicplayersample.utils.SpotifyHelper

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val spotifyButton = findViewById<MaterialButton>(R.id.connectSpotifyQR)
    val textInputEditText = findViewById<TextInputEditText>(R.id.textInputEditText)
    val startButton = findViewById<MaterialButton>(R.id.startButton)

    spotifyButton.setOnClickListener { SpotifyHelper().createAuthRequest(this) }
    startButton.setOnClickListener {
      PreferenceHelper.defaultPrefs(this).edit().putString(getString(R.string.room_name_preference),
          textInputEditText.text.toString()).apply()
      startActivity(Intent(this, BottomNavigationActivity::class.java))
      finish()
    }
  }


  override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
    super.onActivityResult(requestCode, resultCode, intent)
//    for (fragment in supportFragmentManager.fragments) {
//      fragment.onActivityResult(requestCode, resultCode, intent)
//    }    // Check if result comes from the correct activity
    if (requestCode == REQUEST_CODE) {
      val response = AuthenticationClient.getResponse(resultCode, intent)

      when (response.type) {
        AuthenticationResponse.Type.TOKEN -> {
          PreferenceHelper.defaultPrefs(this).edit().putString("token",
              response.accessToken).apply()

        }

        // Auth flow returned an error
        AuthenticationResponse.Type.ERROR -> Log.d("Tag", "Error")
      }

    }
  }
}