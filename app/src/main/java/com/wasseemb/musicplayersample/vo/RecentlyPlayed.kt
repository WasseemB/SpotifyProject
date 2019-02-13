package com.wasseemb.musicplayersample.vo

import com.squareup.moshi.Json


data class RecentlyPlayed(
    @Json(name = "items") val items: List<Item>,
    @Json(name = "next") val next: String,
    @Json(name = "cursors") val cursors: Cursors,
    @Json(name = "limit") val limit: Int,
    @Json(name = "href") val href: String
) {

  data class Item(
      @Json(name = "track") val track: Track,
      @Json(name = "played_at") val playedAt: String,
      @Json(name = "context") val context: Context
  ) {

    data class Context(
        @Json(name = "uri") val uri: String,
        @Json(name = "external_urls") val externalUrls: ExternalUrls,
        @Json(name = "href") val href: String,
        @Json(name = "type") val type: String
    ) {

      data class ExternalUrls(
          @Json(name = "spotify") val spotify: String
      )
    }


    data class Track(
        @Json(name = "artists") val artists: List<Artist>,
        @Json(name = "available_markets") val availableMarkets: List<String>,
        @Json(name = "disc_number") val discNumber: Int,
        @Json(name = "duration_ms") val durationMs: Int,
        @Json(name = "explicit") val explicit: Boolean,
        @Json(name = "external_urls") val externalUrls: ExternalUrls,
        @Json(name = "href") val href: String,
        @Json(name = "id") val id: String,
        @Json(name = "name") val name: String,
        @Json(name = "preview_url") val previewUrl: String,
        @Json(name = "track_number") val trackNumber: Int,
        @Json(name = "type") val type: String,
        @Json(name = "uri") val uri: String
    ) {

      data class ExternalUrls(
          @Json(name = "spotify") val spotify: String
      )


      data class Artist(
          @Json(name = "external_urls") val externalUrls: ExternalUrls,
          @Json(name = "href") val href: String,
          @Json(name = "id") val id: String,
          @Json(name = "name") val name: String,
          @Json(name = "type") val type: String,
          @Json(name = "uri") val uri: String
      ) {

        data class ExternalUrls(
            @Json(name = "spotify") val spotify: String
        )
      }
    }
  }


  data class Cursors(
      @Json(name = "after") val after: String,
      @Json(name = "before") val before: String
  )
}