package com.wasseemb.musicplayersample.vo

import com.squareup.moshi.Json


data class UserResponse(
    @Json(name = "country") val country: String,
    @Json(name = "display_name") val displayName: String,
    @Json(name = "email") val email: String,
    @Json(name = "external_urls") val externalUrls: ExternalUrls,
    @Json(name = "followers") val followers: Followers,
    @Json(name = "href") val href: String,
    @Json(name = "id") val id: String,
    @Json(name = "images") val images: List<Any>,
    @Json(name = "product") val product: String,
    @Json(name = "type") val type: String,
    @Json(name = "uri") val uri: String
) {

  data class ExternalUrls(
      @Json(name = "spotify") val spotify: String
  )


  data class Followers(
      @Json(name = "href") val href: Any?,
      @Json(name = "total") val total: Int
  )
}