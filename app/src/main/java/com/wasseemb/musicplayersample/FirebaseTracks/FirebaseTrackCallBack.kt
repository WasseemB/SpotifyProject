package com.wasseemb.musicplayersample.Track

import androidx.recyclerview.widget.DiffUtil
import com.wasseemb.musicplayersample.vo.FirebaseTrack

class FirebaseTrackCallBack : DiffUtil.ItemCallback<FirebaseTrack>() {
  override fun areItemsTheSame(oldItem: FirebaseTrack,
      newItem: FirebaseTrack): Boolean {
    return oldItem.uri == newItem.uri

  }

  override fun areContentsTheSame(oldItem: FirebaseTrack,
      newItem: FirebaseTrack): Boolean {
    return oldItem == newItem

  }
}