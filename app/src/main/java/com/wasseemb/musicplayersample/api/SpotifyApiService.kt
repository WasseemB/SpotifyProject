package com.wasseemb.musicplayersample.api

import com.wasseemb.musicplayersample.vo.Playlists
import com.wasseemb.musicplayersample.vo.RecentlyPlayed
import com.wasseemb.musicplayersample.vo.SingleTrack
import com.wasseemb.musicplayersample.vo.Tracks
import com.wasseemb.musicplayersample.vo.UserResponse
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
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
      : Observable<SingleTrack>

  @GET("me")
  fun getUserData(): Single<UserResponse>

  @GET("me/player/recently-played")
  fun getRecentlyPlayed(): Single<RecentlyPlayed>


  @POST("users/{user_id}/playlists")
  fun createPlayList(@Path(
      "user_id") userId: String, @Body body: Map<String, String>): Observable<Playlists>


  @POST("playlists/{playlist_id}/tracks")
  fun addTracksToPlaylist(@Path(
      "playlist_id") userId: String, @Body body: Map<String, List<String>>): Observable<ResponseBody>
}