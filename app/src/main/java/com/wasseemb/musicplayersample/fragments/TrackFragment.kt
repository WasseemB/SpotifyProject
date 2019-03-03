package com.wasseemb.musicplayersample.fragments

import android.content.Context
import android.os.Bundle
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
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.wasseemb.musicplayersample.MusicPlayerSampleApplication
import com.wasseemb.musicplayersample.R
import com.wasseemb.musicplayersample.R.drawable
import com.wasseemb.musicplayersample.R.layout
import com.wasseemb.musicplayersample.SpotifyViewModel
import com.wasseemb.musicplayersample.Track.FirebaseTrackAdapter
import com.wasseemb.musicplayersample.Track.FirebaseTrackAdapter.DisplayableTrackClickListener
import com.wasseemb.musicplayersample.extensions.uploadToFirebase
import com.wasseemb.musicplayersample.utils.Helper
import com.wasseemb.musicplayersample.utils.SpotifyHelper
import com.wasseemb.musicplayersample.vo.FirebaseTrack
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FirebaseTrackAdapter.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FirebaseTrackAdapter.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TrackFragment : Fragment(), DisplayableTrackClickListener {
  private var spotifyAppRemote: SpotifyAppRemote? = null

  override fun onItemClick(item: FirebaseTrack) {
    spotifyAppRemote = SpotifyHelper().playTrack(item, context)
  }

  private lateinit var songHashMap: HashMap<String, FirebaseTrack>
  lateinit var trackAdapter: FirebaseTrackAdapter


  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var spotifyViewModel: SpotifyViewModel

  val firebaseTracks = MutableLiveData<List<FirebaseTrack>>()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val rootView = inflater.inflate(layout.trackitemdata_list, container, false)
    val fab = activity?.findViewById<MaterialButton>(R.id.fab)
    fab?.let {
      it.text = "Upload to firebase"
      it.icon = ContextCompat.getDrawable(context!!,
          drawable.ic_upload)
      it.setOnClickListener {
        uploadToFirebase(songHashMap)
      }

    }

    spotifyViewModel = ViewModelProviders.of(this, viewModelFactory)[SpotifyViewModel::class.java]
    firebaseTracks.value = spotifyViewModel.displayableTrack.value

    setupRecyclerView(rootView)
    getTracks()
    return rootView
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    (activity?.application as MusicPlayerSampleApplication).component.inject(this)

  }


  override fun onStop() {
    super.onStop()
    SpotifyAppRemote.disconnect(spotifyAppRemote)
  }

  private fun setupRecyclerView(view: View) {
    val recyclerView = view.findViewById<RecyclerView>(R.id.trackitemdata_list)
    recyclerView.layoutManager = LinearLayoutManager(context,
        RecyclerView.VERTICAL, false)
    trackAdapter = FirebaseTrackAdapter()
    trackAdapter.itemClickListener = this
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
    fun newInstance() =
        TrackFragment().apply {}
  }
}
