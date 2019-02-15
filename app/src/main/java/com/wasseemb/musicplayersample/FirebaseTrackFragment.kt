package com.wasseemb.musicplayersample

import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.wasseemb.musicplayersample.R.layout
import com.wasseemb.musicplayersample.Track.FirebaseTrackAdapter
import com.wasseemb.musicplayersample.Utils.CLIENT_ID
import com.wasseemb.musicplayersample.Utils.REDIRECT_URI
import com.wasseemb.musicplayersample.api.SpotifyApiService
import com.wasseemb.musicplayersample.vo.FirebaseTrack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import nz.co.trademe.covert.Covert


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FirebaseTrackFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FirebaseTrackFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FirebaseTrackFragment : Fragment() {
  lateinit var recyclerView: RecyclerView
  lateinit var trackAdapter: FirebaseTrackAdapter
  // TODO: Rename and change types of parameters
  private var param1: String? = null
  private var param2: String? = null
  private var mSpotifyAppRemote: SpotifyAppRemote? = null
  private var simpleTrackArray = ArrayList<FirebaseTrack>()
  private var uriArray = ArrayList<String>()
  lateinit var materialButton: MaterialButton
  val repository = DummySwipeRepository()


  val covertConfig = Covert.Config(
      iconRes = R.drawable.ic_baseline_arrow_upward, // The icon to show
      iconDefaultColorRes = R.color.white,            // The color of the icon
      actionColorRes = R.color.colorPrimary           // The color of the background
  )

  //private var listener: OnFragmentInteractionListener? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      param1 = it.getString(ARG_PARAM1)
      param2 = it.getString(ARG_PARAM2)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val rootView = inflater.inflate(layout.firebase_list, container, false)
    setupRecyclerView(rootView)
    getTracksFromFirebase()

    materialButton = activity!!.findViewById<MaterialButton>(R.id.fab)
    materialButton.text = "Play firebase playlist"
    materialButton.setOnClickListener {
      playTracks(param2!!)
    }
    return rootView

  }

  private fun getTracksFromFirebase() {
    val database = FirebaseDatabase.getInstance()
    val refdb = database.reference
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
        addTracksToPlaylist(body)
        trackAdapter.submitList(simpleTrackArray)
      }

      override fun onCancelled(databaseError: DatabaseError) {
        println("loadPost:onCancelled ${databaseError.toException()}")
      }
    }
    refdb.child("Songs").addListenerForSingleValueEvent(menuListener)

  }

  private fun addTracksToPlaylist(body: HashMap<String, ArrayList<String>>) {
    SpotifyApiService.create(param1!!).addTracksToPlaylist(param2!!, body).observeOn(
        AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io()).subscribe()
  }

  private fun setupRecyclerView(view: View) {
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
              "Songs/" + simpleTrackArray[viewHolder.adapterPosition].uri).setValue(
              simpleTrackArray[viewHolder.adapterPosition])
          trackAdapter.notifyItemChanged(viewHolder.adapterPosition)

        }
        .attachTo(recyclerView)

    recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    trackAdapter = FirebaseTrackAdapter(covert)
    recyclerView.adapter = trackAdapter
  }

  private fun playTracks(playlistUri: String) {
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
            connected(playlistUri)
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

  private fun connected(playlistUri: String) {
    // Play a playlist
    //mSpotifyAppRemote?.playerApi?.play(item.track.uri)
    Log.d("Connected", playlistUri)
    mSpotifyAppRemote?.playerApi?.play("spotify:playlist:" + playlistUri)


    // Subscribe to PlayerState
    mSpotifyAppRemote?.playerApi
        ?.subscribeToPlayerState()
        ?.setEventCallback { playerState ->
          val track = playerState.track
          if (track != null) {
            Log.d("MainActivity", track.name + " by " + track.artist.name)
            materialButton.text = "Currently Playing : " + track.name

          }
        }
  }

  // TODO: Rename method, update argument and hook method into UI event
//  fun onButtonPressed(uri: Uri) {
//    listener?.onFragmentInteraction(uri)
//  }
//
//  override fun onAttach(context: Context) {
//    super.onAttach(context)
//    if (context is OnFragmentInteractionListener) {
//      listener = context
//    } else {
//      throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//    }
//  }
//
//  override fun onDetach() {
//    super.onDetach()
//    listener = null
//  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   *
   *
   * See the Android Training lesson [Communicating with Other Fragments]
   * (http://developer.android.com/training/basics/fragments/communicating.html)
   * for more information.
   */
//  interface OnFragmentInteractionListener {
//    // TODO: Update argument type and name
//    fun onFragmentInteraction(uri: Uri)
//  }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadedTracksFragment.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(param1: String, param2: String) =
        FirebaseTrackFragment().apply {
          arguments = Bundle().apply {
            putString(ARG_PARAM1, param1)
            putString(ARG_PARAM2, param2)
          }
        }
  }
}
