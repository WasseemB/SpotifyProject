package com.wasseemb.musicplayersample.Track

import androidx.recyclerview.widget.DiffUtil
import com.wasseemb.musicplayersample.vo.Tracks

class TrackCallBack : DiffUtil.ItemCallback<Tracks.Item>() {
  override fun areItemsTheSame(oldItem: Tracks.Item,
      newItem: Tracks.Item): Boolean {
    return oldItem.track.id == newItem.track.id

  }

  override fun areContentsTheSame(oldItem: Tracks.Item,
      newItem: Tracks.Item): Boolean {
    return oldItem == newItem

  }
}