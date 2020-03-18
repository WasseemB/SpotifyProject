package com.wasseemb.musicplayersample.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.wasseemb.musicplayersample.MusicPlayerSampleApplication
import com.wasseemb.musicplayersample.R
import com.wasseemb.musicplayersample.R.color
import com.wasseemb.musicplayersample.R.drawable
import com.wasseemb.musicplayersample.R.layout
import com.wasseemb.musicplayersample.Track.FirebaseTrackAdapter
import com.wasseemb.musicplayersample.Track.FirebaseTrackAdapter.DisplayableTrackClickListener
import com.wasseemb.musicplayersample.extensions.PreferenceHelper
import com.wasseemb.musicplayersample.repositories.DummySwipeRepository
import com.wasseemb.musicplayersample.utils.SpotifyHelper
import com.wasseemb.musicplayersample.vo.FirebaseTrack
import nz.co.trademe.covert.Covert
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "token"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FirebaseTrackFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FirebaseTrackFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class QueueTrackFragment : Fragment(), DisplayableTrackClickListener {
  private var spotifyAppRemote: SpotifyAppRemote? = null

  override fun onItemClick(item: FirebaseTrack) {
    spotifyAppRemote = SpotifyHelper().playTrack(item, context)
  }


  @Inject
  lateinit var databaseReference: DatabaseReference
  lateinit var recyclerView: RecyclerView
  lateinit var trackAdapter: FirebaseTrackAdapter
  // TODO: Rename and change types of parameters
  //private var token: String? = null
  private var simpleTrackArray = ArrayList<FirebaseTrack>()
  private var uriArray = ArrayList<String>()
  lateinit var materialButton: MaterialButton
  val repository = DummySwipeRepository()


  val covertConfig = Covert.Config(
      iconRes = drawable.ic_baseline_arrow_upward, // The icon to show
      iconDefaultColorRes = color.white,            // The color of the icon
      actionColorRes = color.colorPrimary           // The color of the background
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      //token = it.getString(ARG_PARAM1)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val rootView = inflater.inflate(layout.firebase_list, container, false)
    setupRecyclerView(rootView)
    getTracksFromFirebase()

//    materialButton = activity!!.findViewById<MaterialButton>(
//        R.id.fab)
//    materialButton.text = "Play firebase playlist"
////    materialButton.setOnClickListener {
////      playTracks(param2!!)
////    }
    return rootView

  }


  private fun getTracksFromFirebase() {
    val menuListener = object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        simpleTrackArray.clear()
        dataSnapshot.children.mapNotNullTo(simpleTrackArray) {
          it.getValue<FirebaseTrack>(FirebaseTrack::class.java)
        }
        for (track: FirebaseTrack in simpleTrackArray) {
          uriArray.add(track.uri)
        }
        val body = HashMap<String, ArrayList<String>>()
        body["uris"] = uriArray
        simpleTrackArray.sortBy {
          it.name.toLowerCase()
        }

        //addTracksToPlaylist(body)
        trackAdapter.submitList(simpleTrackArray)
      }

      override fun onCancelled(databaseError: DatabaseError) {
        println("loadPost:onCancelled ${databaseError.toException()}")
      }
    }
    val roomName = PreferenceHelper.defaultPrefs(context!!).getString(
        getString(R.string.room_name_preference), "")
    databaseReference.child(roomName).addValueEventListener(menuListener)

  }

//  private fun addTracksToPlaylist(body: HashMap<String, ArrayList<String>>) {
//    SpotifyApiService.create(token!!).addTracksToPlaylist(param2!!, body).observeOn(
//        AndroidSchedulers.mainThread())
//        .subscribeOn(Schedulers.io()).subscribe()
//  }


  override fun onAttach(context: Context) {
    super.onAttach(context)
    (activity?.application as MusicPlayerSampleApplication).component.inject(this)
  }

  private fun setupRecyclerView(view: View) {
    val roomName = PreferenceHelper.defaultPrefs(context!!).getString(
        getString(R.string.room_name_preference), "")
    recyclerView = view.findViewById(R.id.firebase_list)
    val covert = Covert.with(covertConfig)
        .setIsActiveCallback {
          // This is a callback to check if the item is active, i.e checked
          repository.isActive(it.adapterPosition)
        }
        .doOnSwipe { viewHolder, _ ->
          // This callback is fired when a ViewHolder is swiped
          repository.toggleActiveState(viewHolder.adapterPosition)
          Log.d("CurrentPos", viewHolder.adapterPosition.toString())
          if (repository.isActive(viewHolder.adapterPosition))
            simpleTrackArray[viewHolder.adapterPosition].vote++
          else
            simpleTrackArray[viewHolder.adapterPosition].vote--
          val database = FirebaseDatabase.getInstance()
          database.reference.child(
              "${roomName}/" + simpleTrackArray[viewHolder.adapterPosition].uri).setValue(
              simpleTrackArray[viewHolder.adapterPosition])
          trackAdapter.notifyItemChanged(viewHolder.adapterPosition)

        }
        .attachTo(recyclerView)

    recyclerView.layoutManager = LinearLayoutManager(context,
        RecyclerView.VERTICAL, false)
    trackAdapter = FirebaseTrackAdapter(covert)
    trackAdapter.itemClickListener = this
    recyclerView.adapter = trackAdapter
  }


  override fun onStop() {
    super.onStop()
    SpotifyAppRemote.disconnect(spotifyAppRemote)
  }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param authToken Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadedTracksFragment.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance() =
        QueueTrackFragment().apply {
          arguments = Bundle().apply {
            //putString(ARG_PARAM1, authToken)
          }
        }
  }
}
