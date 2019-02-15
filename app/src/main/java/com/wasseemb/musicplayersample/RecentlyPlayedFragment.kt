package com.wasseemb.musicplayersample

import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.wasseemb.musicplayersample.Extensions.uploadToFirebase
import com.wasseemb.musicplayersample.R.layout
import com.wasseemb.musicplayersample.RecentlyPlayed.RecentlyPlayedAdapter
import com.wasseemb.musicplayersample.RecentlyPlayed.RecentlyPlayedAdapter.ItemClickListener
import com.wasseemb.musicplayersample.Utils.CLIENT_ID
import com.wasseemb.musicplayersample.Utils.REDIRECT_URI
import com.wasseemb.musicplayersample.api.SpotifyApiService
import com.wasseemb.musicplayersample.vo.FirebaseTrack
import com.wasseemb.musicplayersample.vo.RecentlyPlayed
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var mSpotifyAppRemote: SpotifyAppRemote? = null

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FirebaseTrackAdapter.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FirebaseTrackAdapter.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RecentlyPlayedFragment : Fragment(), ItemClickListener {
  // TODO: Rename and change types of parameters
  private var token: String? = null
  private lateinit var recyclerView: RecyclerView
  private lateinit var trackAdapter: RecentlyPlayedAdapter
  private lateinit var songHashMap: HashMap<String, FirebaseTrack>


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      token = it.getString(ARG_PARAM1)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val rootView = inflater.inflate(layout.trackitemdata_list, container, false)

    val fab = activity?.findViewById<MaterialButton>(R.id.fab)
    fab?.visibility = View.VISIBLE
    fab?.text = "Upload to firebase"
    fab?.icon = ContextCompat.getDrawable(context!!, R.drawable.ic_upload)
    fab?.setOnClickListener { uploadToFirebase(songHashMap) }
    setupRecyclerView(rootView)
    getRecentlyPlayed()
    return rootView
  }


  override fun onItemClick(item: RecentlyPlayed.Item) {
    val connectionParams = ConnectionParams.Builder(CLIENT_ID)
        .setRedirectUri(REDIRECT_URI)
        .showAuthView(true)
        .build()
    SpotifyAppRemote.connect(context, connectionParams,
        object : Connector.ConnectionListener {

          override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
            mSpotifyAppRemote = spotifyAppRemote
            Log.d("MainActivity", "Connected! Yay!")

            // Now you can start interacting with App Remote
            connected(item)
          }

          override fun onFailure(throwable: Throwable) {
            Log.e("MainActivity", throwable.message, throwable)

            // Something went wrong when attempting to connect! Handle errors here
          }
        })

  }


  override fun onStop() {
    super.onStop()
    SpotifyAppRemote.disconnect(mSpotifyAppRemote)
  }

  private fun connected(item: RecentlyPlayed.Item) {
    // Play a playlist
    mSpotifyAppRemote?.playerApi?.play(item.track.uri)


    // Subscribe to PlayerState
    mSpotifyAppRemote?.playerApi
        ?.subscribeToPlayerState()
        ?.setEventCallback { playerState ->
          val track = playerState.track
          if (track != null) {
            Log.d("MainActivity", track.name + " by " + track.artist.name)
          }
        }
  }


  private fun setupRecyclerView(view: View) {
    recyclerView = view.findViewById(R.id.trackitemdata_list)
    recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    trackAdapter = RecentlyPlayedAdapter()
    trackAdapter.itemClickListener = this
    recyclerView.adapter = trackAdapter
  }


  private fun getRecentlyPlayed() {
    songHashMap = HashMap()
    token?.let {
      SpotifyApiService.create(it).getRecentlyPlayed().observeOn(
          AndroidSchedulers.mainThread())
          .subscribeOn(Schedulers.io())
          //Retain orignal value of the stream before sideeffects
          .flatMap({ tracks ->
            Observable.fromIterable(tracks.items)
          }, { tracks, item -> Pair(tracks, item) })
          .subscribe { result ->
            //simpleTrackArray.clear()
            songHashMap[result.second.track.uri] = FirebaseTrack(
                artist = result.second.track.artists[0].name,
                name = result.second.track.name,
                uri = result.second.track.uri,
                albumImageUrl = "")
            trackAdapter.submitList(result.first.items)
          }
    }
  }

  companion object {
    @JvmStatic
    fun newInstance(token: String) =
        RecentlyPlayedFragment().apply {
          arguments = Bundle().apply {
            putString(ARG_PARAM1, token)
          }
        }
  }
}
