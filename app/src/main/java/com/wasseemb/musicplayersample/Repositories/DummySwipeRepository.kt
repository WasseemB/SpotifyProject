package com.wasseemb.musicplayersample.Repositories

class DummySwipeRepository {

  private val activeStates: MutableMap<Int, Boolean> = hashMapOf()

  fun toggleActiveState(index: Int) {
    activeStates[index] = !(activeStates[index] ?: false)
  }

  fun isActive(index: Int): Boolean = activeStates[index] ?: false
}