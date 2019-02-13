package com.wasseemb.musicplayersample.api

import com.wasseemb.musicplayersample.vo.HeaderInterceptor
import com.wasseemb.musicplayersample.vo.Playlists
import com.wasseemb.musicplayersample.vo.RecentlyPlayed
import com.wasseemb.musicplayersample.vo.Tracks
import com.wasseemb.musicplayersample.vo.Tracks.Item.Track
import com.wasseemb.musicplayersample.vo.UserResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Wasseem on 14/03/2018.
 */
@JvmSuppressWildcards
interface SpotifyApiService {
  @GET("me/tracks")
  fun getUserTracks(@Query("limit") limit: Int = 50)
      : Observable<Tracks>


  @GET("tracks/{id}")
  fun getTrack(@Path("id") id: String)
      : Observable<Track>

  @GET("me")
  fun getUserData(): Observable<UserResponse>

  @GET("me/player/recently-played")
  fun getRecentlyPlayed(): Observable<RecentlyPlayed>


  @POST("users/{user_id}/playlists")
  fun createPlayList(@Path(
      "user_id") userId: String, @Body body: Map<String, String>): Observable<Playlists>


  @POST("playlists/{playlist_id}/tracks")
  fun addTracksToPlaylist(@Path(
      "playlist_id") userId: String, @Body body: Map<String, List<String>>): Observable<ResponseBody>

  companion object {
    private const val API_URL = "https://api.spotify.com/v1/"
    fun create(token: String): SpotifyApiService {
      val logging = HttpLoggingInterceptor()
      logging.level = HttpLoggingInterceptor.Level.BODY
      val httpClient = OkHttpClient.Builder()
      httpClient.addInterceptor(HeaderInterceptor(token = token))
      //httpClient.addInterceptor(logging)


      val retrofit = Retrofit.Builder()
          .addConverterFactory(
              MoshiConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .baseUrl(API_URL)
          .client(httpClient.build())
          .build()
      return retrofit.create(SpotifyApiService::class.java)
    }
  }

}