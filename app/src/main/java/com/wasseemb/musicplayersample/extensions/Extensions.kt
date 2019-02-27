package com.wasseemb.musicplayersample.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.wasseemb.musicplayersample.R
import com.wasseemb.musicplayersample.vo.FirebaseTrack

/**
 * Created by Wasseem on 23/03/2018.
 */


fun ImageView.loadUrl(url: String?) {
  if (url == "") {
    Picasso.get().load(R.color.colorPrimary).into(this)
  } else
    Picasso.get().load(url).into(this)


}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
  return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}


fun uploadToFirebase(firebaseArray: Map<String, FirebaseTrack>) {
  val rootRef = FirebaseDatabase.getInstance().reference
  rootRef.child("Songs").updateChildren(firebaseArray)
  //rootRef.child("Songs").push().setValue(firebaseArray)
//      .addOnCompleteListener {
//        Snackbar.make(view!!, "Upload Complete", Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show()
//      }

}




