package com.wasseemb.musicplayersample

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class BottomNavigationFABBehavior(@Nullable context: Context, @Nullable attrs: AttributeSet) : CoordinatorLayout.Behavior<FloatingActionButton>(
    context, attrs) {

  override fun layoutDependsOn(@Nullable parent: CoordinatorLayout, @NonNull child: FloatingActionButton, @NonNull dependency: View): Boolean {
    return dependency is Snackbar.SnackbarLayout
  }

  override fun onDependentViewRemoved(@NonNull parent: CoordinatorLayout, @NonNull child: FloatingActionButton, @NonNull dependency: View) {
    child.translationY = 0.0f
  }

  override fun onDependentViewChanged(@NonNull parent: CoordinatorLayout, @NonNull child: FloatingActionButton, @NonNull dependency: View): Boolean {
    return this.updateButton(child, dependency)
  }

  private fun updateButton(child: View, dependency: View): Boolean {
    if (dependency is Snackbar.SnackbarLayout) {
      val oldTranslation = child.getTranslationY()
      val height = dependency.getHeight() as Float
      val newTranslation = dependency.getTranslationY() - height
      child.setTranslationY(newTranslation)
      return oldTranslation != newTranslation
    } else {
      return false
    }
  }
}