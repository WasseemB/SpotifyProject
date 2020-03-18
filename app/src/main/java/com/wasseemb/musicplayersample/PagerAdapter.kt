package com.wasseemb.musicplayersample

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wasseemb.musicplayersample.fragments.QueueTrackFragment
import com.wasseemb.musicplayersample.fragments.RecentlyPlayedFragment

class PagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
  override fun getItem(position: Int): Fragment {
    when (position) {
      0 -> return RecentlyPlayedFragment() //ChildFragment2 at position 1
      1 -> return QueueTrackFragment() //ChildFragment3 at position 2
    }
    return QueueTrackFragment()
  }


  override fun getCount(): Int {
    return 2
  }
}