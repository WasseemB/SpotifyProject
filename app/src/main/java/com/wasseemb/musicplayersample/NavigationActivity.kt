package com.wasseemb.musicplayersample

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
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
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_navigation)
    setSupportActionBar(toolbar)

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show()
      //SpotifyHelper().createAuthRequest(this)
    }

    fab.text = "Connect to Spotify"
    fab.setOnClickListener {
      SpotifyHelper().createAuthRequest(this)
      nav_view.setCheckedItem(R.id.nav_recent)
      fab.visibility = View.INVISIBLE
    }


    val toggle = ActionBarDrawerToggle(
        this, drawer_layout, toolbar, R.string.navigation_drawer_open,
        R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    nav_view.setNavigationItemSelectedListener(this)
  }

  private fun setUpUserData(authToken: String) {
    SpotifyApiService.create(authToken).getUserData().observeOn(
        AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io()).subscribe {
          val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
          val headerView = navigationView.getHeaderView(0)
          val navUsername = headerView.findViewById(R.id.nav_header_title) as TextView
          val navMail = headerView.findViewById(R.id.nav_header_mail) as TextView

          navUsername.text = it.id
          navMail.text = it.email
          createPlayList(authToken, it.id)
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
          setUpFragment(authToken)

        }

        // Auth flow returned an error
        AuthenticationResponse.Type.ERROR -> Log.d("Tag", "Error")
      }

    }
  }

  private fun setUpFragment(authToken: String) {
    val newFragment: Fragment
    val transaction = supportFragmentManager.beginTransaction()
    newFragment = RecentlyPlayedFragment.newInstance(authToken)
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
        newFragment = RecentlyPlayedFragment.newInstance(authToken)
      }
      R.id.nav_client -> {
        newFragment = TrackFragment.newInstance(authToken)
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


}
