package com.wasseemb.musicplayersample.dagger

import com.wasseemb.musicplayersample.api.SpotifyApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


@Module(includes = [NetworkModule::class])
class SpotifyServiceModule {

  @Provides
  @ApplicationScope
  fun spotifyService(retrofit: Retrofit): SpotifyApiService {
    return retrofit.create(SpotifyApiService::class.java)
  }

  @Provides
  @ApplicationScope
  fun retrofit(okHttpClient: OkHttpClient): Retrofit {
    val API_URL = "https://api.spotify.com/v1/"
    return Retrofit.Builder()
        .addConverterFactory(
            MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(API_URL)
        .client(okHttpClient)
        .build()
  }
}