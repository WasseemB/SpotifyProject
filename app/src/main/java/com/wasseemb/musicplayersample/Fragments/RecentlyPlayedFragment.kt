package com.wasseemb.musicplayersample.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.wasseemb.musicplayersample.R
import com.wasseemb.musicplayersample.R.drawable
import com.wasseemb.musicplayersample.R.layout
import com.wasseemb.musicplayersample.RecentlyPlayed.RecentlyPlayedAdapter
import com.wasseemb.musicplayersample.RecentlyPlayed.RecentlyPlayedAdapter.ItemClickListener
import com.wasseemb.musicplayersample.SpotifyRepository
import com.wasseemb.musicplayersample.Utils.Helper
import com.wasseemb.musicplayersample.Utils.SpotifyHelper
import com.wasseemb.musicplayersample.vo.FirebaseTrack
import com.wasseemb.musicplayersample.vo.RecentlyPlayed


class RecentlyPlayedFragment : Fragment(), ItemClickListener {
  // TODO: Rename and change types of parameters
  private lateinit var recyclerView: RecyclerView
  private lateinit var trackAdapter: RecentlyPlayedAdapter
  private lateinit var songHashMap: HashMap<String, FirebaseTrack>
  lateinit var spotifyRepository: SpotifyRepository
  private var spotifyAppRemote: SpotifyAppRemote? = null


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
    setupRecyclerView(rootView)
    getRecentlyPlayed()
    return rootView
  }


  override fun onItemClick(item: RecentlyPlayed.Item) {
    spotifyAppRemote = SpotifyHelper().playTrack(item, context)
  }


  override fun onStop() {
    super.onStop()
    SpotifyAppRemote.disconnect(spotifyAppRemote)
  }


  private fun setupRecyclerView(view: View) {
    recyclerView = view.findViewById(R.id.trackitemdata_list)
    recyclerView.layoutManager = LinearLayoutManager(context,
        RecyclerView.VERTICAL, false)
    trackAdapter = RecentlyPlayedAdapter()
    trackAdapter.itemClickListener = this
    recyclerView.adapter = trackAdapter
  }

  private fun getRecentlyPlayed() {
    spotifyRepository.getRecentlyPlayed().subscribe { result ->
      trackAdapter.submitList(result.items)
      songHashMap = Helper().createFirebaseTrackMap(result.items)
    }
  }

  companion object {
    @JvmStatic
    fun newInstance(spotifyRepository: SpotifyRepository) =
        RecentlyPlayedFragment().apply {
          this.spotifyRepository = spotifyRepository
        }
  }
}
