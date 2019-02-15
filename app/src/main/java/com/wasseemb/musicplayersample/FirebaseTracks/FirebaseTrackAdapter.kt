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
import com.wasseemb.musicplayersample.Track.FirebaseTrackAdapter.ListViewHolder
import com.wasseemb.musicplayersample.vo.FirebaseTrack
import nz.co.trademe.covert.Covert


class FirebaseTrackAdapter(private val covert: Covert) : ListAdapter<FirebaseTrack, ListViewHolder>(
    FirebaseTrackCallBack()) {


  override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): ListViewHolder {

    val viewHolder = ListViewHolder(parent.inflate(
        layout.firebase_list_content))
    //viewHolder.itemView.setOnClickListener {
    //val position = viewHolder.adapterPosition
    //if (position != RecyclerView.NO_POSITION)
    //getItem given by implementing ListAdapter(Support Library)
    //itemClickListener.onItemClick(getItem(position)!!)
    //}
    return viewHolder
  }

  override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
    covert.drawCornerFlag(holder)
    holder.bindTo(getItem(position)!!)
  }

  inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackTitleTextView: TextView = itemView.findViewById(
        id.tvTrackTitle)
    private val trackArtistTextView: TextView = itemView.findViewById(
        id.tvTrackArtist)
    private val tvTrackVote: TextView = itemView.findViewById(
        id.tvTrackVote)
    private val trackAlbumImageView: ImageView = itemView.findViewById(
        id.imgAlbumImage)


    fun bindTo(item: FirebaseTrack) {
      trackTitleTextView.text = item.name
      trackArtistTextView.text = item.artist
      tvTrackVote.text = item.vote.toString()
      trackAlbumImageView.loadUrl(item.albumImageUrl)

    }
  }
}
