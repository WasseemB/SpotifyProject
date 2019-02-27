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
import com.wasseemb.musicplayersample.utils.Helper
import com.wasseemb.musicplayersample.utils.SpotifyHelper
import com.wasseemb.musicplayersample.vo.FirebaseTrack
import javax.inject.Inject


class RecentlyPlayedFragment : Fragment(), DisplayableTrackClickListener {
  override fun onItemClick(item: FirebaseTrack) {
    spotifyAppRemote = SpotifyHelper().playTrack(item, context)
  }

  private lateinit var trackAdapter: FirebaseTrackAdapter

  private lateinit var songHashMap: HashMap<String, FirebaseTrack>
  private var spotifyAppRemote: SpotifyAppRemote? = null

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var spotifyViewModel: SpotifyViewModel


  val firebaseRecents = MutableLiveData<List<FirebaseTrack>>()


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val rootView = inflater.inflate(layout.trackitemdata_list, container, false)

    val fab = activity?.findViewById<MaterialButton>(R.id.fab)
    fab?.visibility = View.VISIBLE
    fab?.text = "Upload to firebase"
    fab?.icon = ContextCompat.getDrawable(context!!,
        drawable.ic_upload)
    //fab?.setOnClickListener { uploadToFirebase(songHashMap) }
    spotifyViewModel = ViewModelProviders.of(this, viewModelFactory)[SpotifyViewModel::class.java]
    firebaseRecents.value = spotifyViewModel.displayableRecent.value
    setupRecyclerView(rootView)
    getRecentlyPlayed()
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


  private fun getRecentlyPlayed() {
    spotifyViewModel.loadRecents()
    spotifyViewModel.getLocalRecents().observe(viewLifecycleOwner,
        Observer<List<FirebaseTrack>> {
          trackAdapter.submitList(it)
          songHashMap = Helper().hashMapFromFirebaseTrack(it)
        })

  }


  companion object {
    @JvmStatic
    fun newInstance() =
        RecentlyPlayedFragment().apply {
        }
  }
}
