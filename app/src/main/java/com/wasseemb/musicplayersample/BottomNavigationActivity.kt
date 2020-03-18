package com.wasseemb.musicplayersample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wasseemb.musicplayersample.api.HeaderInterceptor
import com.wasseemb.musicplayersample.dagger.DaggerApplicationComponent
import com.wasseemb.musicplayersample.dagger.HeaderInterceptorModule
import com.wasseemb.musicplayersample.dagger2.ContextModule
import com.wasseemb.musicplayersample.extensions.PreferenceHelper
import kotlinx.android.synthetic.main.activity_bottom_navigation.navigation
import kotlinx.android.synthetic.main.app_bar_navigation.toolbar


class BottomNavigationActivity : AppCompatActivity() {
  lateinit var viewPager: ViewPager

  private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
    when (item.itemId) {
      R.id.navigation_home -> {
        viewPager.currentItem = 0
        return@OnNavigationItemSelectedListener true
      }
      R.id.navigation_dashboard -> {
        viewPager.currentItem = 1
        return@OnNavigationItemSelectedListener true
      }
    }
    false
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_bottom_navigation)
    setSupportActionBar(toolbar)
    setUpInjection()
    viewPager = findViewById<ViewPager>(R.id.viewPager)
    val viewPagerAdapter = PagerAdapter(supportFragmentManager)
    viewPager.adapter = viewPagerAdapter

    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
  }

  private fun setUpInjection() {
    val token = PreferenceHelper.defaultPrefs(this).getString("token", "")
    val interceptor = HeaderInterceptor(token)
    val appComponent = application as MusicPlayerSampleApplication
    appComponent.component = DaggerApplicationComponent.builder().contextModule(
        ContextModule(this)).headerInterceptorModule(
        HeaderInterceptorModule(interceptor)).build()
    appComponent.component.inject(this)
  }
}
