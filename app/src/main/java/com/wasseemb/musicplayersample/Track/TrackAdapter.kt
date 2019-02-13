package com.wasseemb.musicplayersample.Track

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.wasseemb.musicplayersample.Extensions.inflate
import com.wasseemb.musicplayersample.Extensions.loadUrl
import com.wasseemb.musicplayersample.R.id
import com.wasseemb.musicplayersample.R.layout
import com.wasseemb.musicplayersample.Track.TrackAdapter.ListViewHolder
import com.wasseemb.musicplayersample.vo.Tracks


class TrackAdapter : ListAdapter<Tracks.Item, ListViewHolder>(
    TrackCallBack()) {
  lateinit var itemClickListener: ItemClickListener

  interface ItemClickListener {
    fun onItemClick(item: Tracks.Item)
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


    fun bindTo(item: Tracks.Item) {
      trackTitleTextView.text = item.track.name
      trackArtistTextView.text = item.track.artists[0].name
      trackAlbumImageView.loadUrl(item.track.album.images[1].url)

    }
  }
}
