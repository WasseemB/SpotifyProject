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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.wasseemb.musicplayersample.MusicPlayerSampleApplication
import com.wasseemb.musicplayersample.R
import com.wasseemb.musicplayersample.R.drawable
import com.wasseemb.musicplayersample.R.layout
import com.wasseemb.musicplayersample.SpotifyViewModel
import com.wasseemb.musicplayersample.Track.FirebaseTrackAdapter
import com.wasseemb.musicplayersample.Track.FirebaseTrackAdapter.DisplayableTrackClickListener
import com.wasseemb.musicplayersample.extensions.PreferenceHelper
import com.wasseemb.musicplayersample.extensions.uploadToFirebase
import com.wasseemb.musicplayersample.utils.Helper
import com.wasseemb.musicplayersample.utils.SpotifyHelper
import com.wasseemb.musicplayersample.vo.FirebaseTrack
import javax.inject.Inject


class RecentlyPlayedFragment @Inject constructor() : Fragment(), DisplayableTrackClickListener {
  override fun onItemClick(item: FirebaseTrack) {
    spotifyAppRemote = SpotifyHelper().playTrack(item, context)
  }

  private lateinit var trackAdapter: FirebaseTrackAdapter
  private var spotifyAppRemote: SpotifyAppRemote? = null

  @Inject
  lateinit var spotifyViewModel: SpotifyViewModel


  @Inject
  lateinit var databaseReference: DatabaseReference
  private var uriArray = ArrayList<String>()

  val firebaseRecents = MutableLiveData<List<FirebaseTrack>>()


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val rootView = inflater.inflate(layout.firebase_list, container, false)

    val fab = activity?.findViewById<MaterialButton>(R.id.fab)
    fab?.visibility = View.VISIBLE
    fab?.text = "Upload to firebase"
    fab?.icon = ContextCompat.getDrawable(context!!,
        drawable.ic_upload)
    fab?.setOnClickListener {
      uploadWithHandlingDuplicates()
      // songHashMap = Helper().hashMapFromFirebaseTrack(firebaseRecents.value!!)
      //uploadToFirebase(songHashMap)
    }
    //spotifyViewModel = ViewModelProviders.of(this, viewModelFactory)[SpotifyViewModel::class.java]
    firebaseRecents.value = spotifyViewModel.displayableAll.value
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
    val recyclerView = view.findViewById<RecyclerView>(R.id.firebase_list)
    recyclerView.layoutManager = LinearLayoutManager(context,
        RecyclerView.VERTICAL, false) as LayoutManager?
    trackAdapter = FirebaseTrackAdapter()
    trackAdapter.itemClickListener = this
    recyclerView.adapter = trackAdapter
  }


  private fun getRecentlyPlayed() {
    spotifyViewModel.loadRecents()
    spotifyViewModel.loadTracks()

    spotifyViewModel.getLocal().observe(viewLifecycleOwner,
        Observer<List<FirebaseTrack>> {
          trackAdapter.submitList(it.sortedBy { it.name })
          //firebaseRecents.postValue(it)
          //songHashMap = Helper().hashMapFromFirebaseTrack(it)
          firebaseRecents.value = it
        })

  }

  private fun uploadWithHandlingDuplicates() {
    var songHashMap = HashMap<String, FirebaseTrack>()
    var simpleTrackArray = ArrayList<FirebaseTrack>()

    val roomName = PreferenceHelper.defaultPrefs(context!!).getString(
        getString(R.string.room_name_preference), "")
    databaseReference.child(roomName).runTransaction(object : Transaction.Handler {
      override fun doTransaction(mutableData: MutableData): Transaction.Result {
        simpleTrackArray.clear()
        val p = mutableData.children.mapNotNullTo(simpleTrackArray) {
          it.getValue<FirebaseTrack>(FirebaseTrack::class.java)
        }
        songHashMap = Helper().hashMapFromFirebaseTrack(p)
        if (!songHashMap.isEmpty()) {
          firebaseRecents.value!!.forEach {
            if (songHashMap.contains(it.uri)) {
              (songHashMap[it.uri] as FirebaseTrack).amount++
            }
          }
        } else
          songHashMap = Helper().hashMapFromFirebaseTrack(firebaseRecents.value!!)

        return Transaction.success(mutableData)
      }

      override fun onComplete(databaseError: DatabaseError?, b: Boolean,
          dataSnapshot: DataSnapshot?) {
        uploadToFirebase(songHashMap, roomName)

      }
    })
  }


  companion object {
    @JvmStatic
    fun newInstance() =
        RecentlyPlayedFragment().apply {
        }
  }
}
