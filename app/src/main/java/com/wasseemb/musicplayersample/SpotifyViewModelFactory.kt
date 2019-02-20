package com.wasseemb.musicplayersample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class SpotifyViewModelFactory(private val repo: SpotifyRepository) : ViewModelProvider.NewInstanceFactory() {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return SpotifyViewModel(repo) as T
  }
}