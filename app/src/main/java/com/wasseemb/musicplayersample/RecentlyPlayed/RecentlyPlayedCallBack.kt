package com.wasseemb.musicplayersample.RecentlyPlayed

import androidx.recyclerview.widget.DiffUtil
import com.wasseemb.musicplayersample.vo.RecentlyPlayed

class RecentlyPlayedCallBack : DiffUtil.ItemCallback<RecentlyPlayed.Item>() {
  override fun areItemsTheSame(oldItem: RecentlyPlayed.Item,
      newItem: RecentlyPlayed.Item): Boolean {
    return oldItem.track.uri == newItem.track.uri

  }

  override fun areContentsTheSame(oldItem: RecentlyPlayed.Item,
      newItem: RecentlyPlayed.Item): Boolean {
    return oldItem == newItem

  }
}