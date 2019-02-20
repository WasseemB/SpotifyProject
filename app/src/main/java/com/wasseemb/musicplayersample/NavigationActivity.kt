package com.wasseemb.musicplayersample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.google.android.material.navigation.NavigationView
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.wasseemb.musicplayersample.Database.FirebaseTracksDatabase
import com.wasseemb.musicplayersample.Fragments.FirebaseTrackFragment
import com.wasseemb.musicplayersample.Fragments.QRFragment
import com.wasseemb.musicplayersample.Fragments.QRReaderFragment
import com.wasseemb.musicplayersample.Fragments.RecentlyPlayedFragment
import com.wasseemb.musicplayersample.Fragments.TrackFragment
import com.wasseemb.musicplayersample.Utils.REQUEST_CODE
import com.wasseemb.musicplayersample.Utils.SpotifyHelper
import com.wasseemb.musicplayersample.api.SpotifyApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_navigation.drawer_layout
import kotlinx.android.synthetic.main.activity_navigation.nav_view
import kotlinx.android.synthetic.main.app_bar_navigation.fab
import kotlinx.android.synthetic.main.app_bar_navigation.toolbar


class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


  private lateinit var authToken: String
  private lateinit var playlistId: String
  private lateinit var spotifyRepository: SpotifyRepository
  private lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var spotifyViewModel: SpotifyViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_navigation)
    setSupportActionBar(toolbar)

    fab.text = "Connect to Spotify"
    fab.setOnClickListener {
      SpotifyHelper().createAuthRequest(this)
      nav_view.setCheckedItem(R.id.nav_recent)
      //fab.visibility = View.INVISIBLE
    }

    val toggle = ActionBarDrawerToggle(
        this, drawer_layout, toolbar, R.string.navigation_drawer_open,
        R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    nav_view.setNavigationItemSelectedListener(this)
  }

  private fun setUpUserData(authToken: String) {
    spotifyRepository = SpotifyRepository(SpotifyApiService.create(authToken),
        provideDatabase(this).firebaseDao())
    viewModelFactory = SpotifyViewModelFactory(spotifyRepository)
    spotifyViewModel = ViewModelProviders.of(this, viewModelFactory).get(
        SpotifyViewModel::class.java)

    val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
    val headerView = navigationView.getHeaderView(0)
    val navUsername = headerView.findViewById(R.id.nav_header_title) as TextView
    val navMail = headerView.findViewById(R.id.nav_header_mail) as TextView
    spotifyRepository.getUserData().subscribe { userResponse ->
      navUsername.text = userResponse.id
      navMail.text = userResponse.email
      //createPlayList(authToken, userResponse.id)
    }

  }

  private fun createPlayList(authToken: String, userId: String) {
    val body = HashMap<String, String>()
    body["name"] = "Temp"
    SpotifyApiService.create(authToken).createPlayList(userId, body).observeOn(
        AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io()).subscribe {
          Log.d("CreatePlaylist", it.id)
          playlistId = it.id
        }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
    super.onActivityResult(requestCode, resultCode, intent)

    // Check if result comes from the correct activity
    if (requestCode == REQUEST_CODE) {
      val response = AuthenticationClient.getResponse(resultCode, intent)

      when (response.type) {
        AuthenticationResponse.Type.TOKEN -> {
          authToken = response.accessToken
          setUpUserData(authToken)
          setUpFragment()

        }

        // Auth flow returned an error
        AuthenticationResponse.Type.ERROR -> Log.d("Tag", "Error")
      }

    }
  }

  private fun setUpFragment() {
    val newFragment: Fragment
    val transaction = supportFragmentManager.beginTransaction()
    newFragment = RecentlyPlayedFragment.newInstance(spotifyRepository)
    transaction.replace(R.id.content_data, newFragment)
    transaction.addToBackStack(null)
    transaction.commit()
  }

  override fun onBackPressed() {
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.navigation, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    when (item.itemId) {
      R.id.action_settings -> return true
      else -> return super.onOptionsItemSelected(item)
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    // Handle navigation view item clicks here.
    var newFragment = Fragment()
    val transaction = supportFragmentManager.beginTransaction()
    when (item.itemId) {
      R.id.nav_recent -> {
        newFragment = RecentlyPlayedFragment.newInstance(spotifyRepository)
      }
      R.id.nav_client -> {
        newFragment = TrackFragment.newInstance(spotifyRepository)
      }
      R.id.nav_player -> {
        newFragment = FirebaseTrackFragment.newInstance(authToken, playlistId)
      }

      R.id.qr_fragment -> {
        newFragment = QRFragment.newInstance("", "")
      }
      R.id.qr_reader -> {
        newFragment = QRReaderFragment.newInstance("", "")
      }
    }
    transaction.replace(R.id.content_data, newFragment)
    transaction.commit()

    drawer_layout.closeDrawer(GravityCompat.START)
    return true
  }

  fun provideDatabase(context: Context): FirebaseTracksDatabase {

    return Room.databaseBuilder(context, FirebaseTracksDatabase::class.java,
        "firebasetrackdb.db").build()

  }


}
