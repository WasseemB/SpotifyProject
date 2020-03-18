package com.wasseemb.musicplayersample

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class BottomNavigationBehavior(@NonNull context: Context, @NonNull attrs: AttributeSet) : CoordinatorLayout.Behavior<BottomNavigationView>(
    context, attrs) {

  fun layoutDependsOn(@Nullable parent: CoordinatorLayout?, @NonNull child: BottomNavigationView, @Nullable dependency: View?): Boolean {
    if (dependency is Snackbar.SnackbarLayout) {
      this.updateSnackbar(child, (dependency as Snackbar.SnackbarLayout?)!!)
    }

    assert(parent != null)
    assert(dependency != null)
    return super.layoutDependsOn(parent!!, child, dependency!!)
  }

  override fun onStartNestedScroll(@NonNull coordinatorLayout: CoordinatorLayout, @NonNull child: BottomNavigationView, @NonNull directTargetChild: View, @NonNull target: View,
      axes: Int, type: Int): Boolean {
    return axes == ViewCompat.SCROLL_AXIS_VERTICAL
  }

  override fun onNestedPreScroll(@NonNull coordinatorLayout: CoordinatorLayout, @NonNull child: BottomNavigationView, @NonNull target: View,
      dx: Int, dy: Int, @NonNull consumed: IntArray, type: Int) {
    super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    child.translationY = Math.max(0.0f, Math.min(child.height.toFloat(), child.translationY + dy))
  }

  private fun updateSnackbar(child: BottomNavigationView, snackbarLayout: Snackbar.SnackbarLayout) {
    if (snackbarLayout.layoutParams is CoordinatorLayout.LayoutParams) {
      val layoutParams = snackbarLayout.layoutParams ?: throw RuntimeException(
          "null cannot be cast to non-null type android.support.design.widget.CoordinatorLayout.LayoutParams")

      val params = layoutParams as CoordinatorLayout.LayoutParams
      params.anchorId = child.id
      params.anchorGravity = Gravity.TOP
      params.gravity = Gravity.TOP
      snackbarLayout.layoutParams = params
    }

  }
}