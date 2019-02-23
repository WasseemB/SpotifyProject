package com.wasseemb.musicplayersample.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.wasseemb.musicplayersample.Extensions.uploadToFirebase
import com.wasseemb.musicplayersample.MusicPlayerSampleApplication
import com.wasseemb.musicplayersample.R
import com.wasseemb.musicplayersample.R.drawable
import com.wasseemb.musicplayersample.R.layout
import com.wasseemb.musicplayersample.SpotifyRepository
import com.wasseemb.musicplayersample.SpotifyViewModel
import com.wasseemb.musicplayersample.Track.FirebaseTrackAdapter
import com.wasseemb.musicplayersample.Track.TrackAdapter.ItemClickListener
import com.wasseemb.musicplayersample.Utils.CLIENT_ID
import com.wasseemb.musicplayersample.Utils.Helper
import com.wasseemb.musicplayersample.Utils.REDIRECT_URI
import com.wasseemb.musicplayersample.vo.FirebaseTrack
import com.wasseemb.musicplayersample.vo.Tracks.Item


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
class TrackFragment : Fragment(), ItemClickListener {

  // TODO: Rename and change types of parameters
  //private var token: String? = null
  //private var listener: OnFragmentInteractionListener? = null
  //private var simpleTrackArray = ArrayList<FirebaseTrack>()
  private lateinit var songHashMap: HashMap<String, FirebaseTrack>
  lateinit var recyclerView: RecyclerView
  lateinit var trackAdapter: FirebaseTrackAdapter
  lateinit var spotifyRepository: SpotifyRepository

  val firebaseTracks = MutableLiveData<List<FirebaseTrack>>()

  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var spotifyViewModel: SpotifyViewModel


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //firebaseTracks.value = spotifyRepository.getSpotifyTracks().value
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val rootView = inflater.inflate(layout.trackitemdata_list, container, false)
    val fab = activity?.findViewById<MaterialButton>(R.id.fab)
    fab?.text = "Upload to firebase"
    fab?.icon = ContextCompat.getDrawable(context!!,
        drawable.ic_upload)
    fab?.setOnClickListener {
      uploadToFirebase(songHashMap)
    }
    //viewModelFactory = SpotifyViewModelFactory(spotifyRepository)
    val appComponent = activity?.application as MusicPlayerSampleApplication

    viewModelFactory = appComponent.component.getViewModelFactory()

    spotifyViewModel = ViewModelProviders.of(this, viewModelFactory)[SpotifyViewModel::class.java]
    setupRecyclerView(rootView)
    getTracks()
    return rootView
  }


  override fun onItemClick(item: Item) {
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

  private fun connected(item: Item) {
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
    recyclerView.layoutManager = LinearLayoutManager(context,
        RecyclerView.VERTICAL, false)
    trackAdapter = FirebaseTrackAdapter()
    // trackAdapter.itemClickListener = this
    recyclerView.adapter = trackAdapter
  }


  private fun getTracks() {
    spotifyViewModel.loadTracks()
    spotifyViewModel.getLocalTracks().observe(viewLifecycleOwner,
        Observer<List<FirebaseTrack>> {
          trackAdapter.submitList(it)
          songHashMap = Helper().hashMapFromFirebaseTrack(it)
        })

  }


  companion object {
    @JvmStatic
    fun newInstance(spotifyRepository: SpotifyRepository) =
        TrackFragment().apply {
          this.spotifyRepository = spotifyRepository
        }
  }
}
