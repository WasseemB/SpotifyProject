package com.wasseemb.musicplayersample.RecentlyPlayed

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wasseemb.musicplayersample.Extensions.inflate
import com.wasseemb.musicplayersample.R.id
import com.wasseemb.musicplayersample.R.layout
import com.wasseemb.musicplayersample.RecentlyPlayed.RecentlyPlayedAdapter.ListViewHolder
import com.wasseemb.musicplayersample.vo.RecentlyPlayed


class RecentlyPlayedAdapter : ListAdapter<RecentlyPlayed.Item, ListViewHolder>(
    RecentlyPlayedCallBack()) {
  lateinit var itemClickListener: ItemClickListener

  interface ItemClickListener {
    fun onItemClick(item: RecentlyPlayed.Item)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): ListViewHolder {

    val viewHolder = ListViewHolder(parent.inflate(
        layout.trackitemdata_list_content))
    viewHolder.itemView.setOnClickListener {
      val position = viewHolder.adapterPosition
      if (position != RecyclerView.NO_POSITION)
      //getItem given by implementing ListAdapter(Support Library)
        itemClickListener.onItemClick(getItem(position)!!)
    }
    return viewHolder
  }

  override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
    holder.bindTo(getItem(position)!!)
  }

  inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackTitleTextView: TextView = itemView.findViewById(
        id.tvTrackTitle)
    private val trackArtistTextView: TextView = itemView.findViewById(
        id.tvTrackArtist)
    private val trackAlbumImageView: ImageView = itemView.findViewById(
        id.imgAlbumImage)


    fun bindTo(item: RecentlyPlayed.Item) {
      trackTitleTextView.text = item.track.name
      trackArtistTextView.text = item.track.artists[0].name
      //trackAlbumImageView.loadUrl(item.track.externalUrls)

    }
  }
}
