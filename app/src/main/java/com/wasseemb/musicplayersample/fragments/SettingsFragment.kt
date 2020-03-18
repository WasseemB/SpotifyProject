package com.wasseemb.musicplayersample.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.wasseemb.musicplayersample.MusicPlayerSampleApplication
import com.wasseemb.musicplayersample.R
import com.wasseemb.musicplayersample.R.string
import com.wasseemb.musicplayersample.R.xml
import com.wasseemb.musicplayersample.SpotifyViewModel
import com.wasseemb.musicplayersample.extensions.PreferenceHelper
import javax.inject.Inject

class SettingsFragment @Inject constructor() : PreferenceFragmentCompat() {

  @Inject
  lateinit var spotifyViewModel: SpotifyViewModel
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(xml.preferences, rootKey)

//    findPreference(getString(R.string.spotify_preference)).setOnPreferenceClickListener {
//      SpotifyHelper().createAuthRequest(this)
//      true
//    }
    (findPreference(getString(
        string.room_name_preference)) as EditTextPreference)
        .setOnPreferenceChangeListener { preference, newValue ->
          preference.summary = newValue.toString()
          PreferenceHelper.defaultPrefs(context!!).edit().putString(preference.key,
              newValue.toString()).commit()
        }

    findPreference(getString(string.generate_preference))
        .setOnPreferenceClickListener {
          val transaction = fragmentManager?.beginTransaction()
          val newFragment = QRFragment.newInstance()
          transaction?.replace(R.id.container, newFragment)
          transaction?.addToBackStack(null)
          transaction?.commit()
          true
        }
    findPreference(getString(string.qr_scan_preference)).setOnPreferenceClickListener {
      val transaction = fragmentManager?.beginTransaction()
      val newFragment = QRReaderFragment.newInstance("", "")
      transaction?.replace(R.id.container, newFragment)
      transaction?.addToBackStack(null)
      transaction?.commit()
      true
    }
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    (activity?.application as MusicPlayerSampleApplication).component.inject(this)

  }

}