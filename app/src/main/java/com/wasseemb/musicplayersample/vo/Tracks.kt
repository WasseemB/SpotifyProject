package com.wasseemb.musicplayersample.vo

import com.squareup.moshi.Json

data class Tracks(
    @Json(name = "href") val href: String,
    @Json(name = "items") val items: List<Item>,
    @Json(name = "limit") val limit: Int,
    @Json(name = "next") val next: String,
    @Json(name = "offset") val offset: Int,
    @Json(name = "previous") val previous: Any?,
    @Json(name = "total") val total: Int
) {

  data class Item(
      @Json(name = "added_at") val addedAt: String,
      @Json(name = "track") val track: Track
  ) {

    data class Track(
        @Json(name = "album") val album: Album,
        @Json(name = "artists") val artists: List<Artist>,
        @Json(name = "available_markets") val availableMarkets: List<String>,
        @Json(name = "disc_number") val discNumber: Int,
        @Json(name = "duration_ms") val durationMs: Int,
        @Json(name = "explicit") val explicit: Boolean,
        @Json(name = "external_ids") val externalIds: ExternalIds,
        @Json(name = "external_urls") val externalUrls: ExternalUrls,
        @Json(name = "href") val href: String,
        @Json(name = "id") val id: String,
        @Json(name = "is_local") val isLocal: Boolean,
        @Json(name = "name") val name: String,
        @Json(name = "popularity") val popularity: Int,
        @Json(name = "preview_url") val previewUrl: String,
        @Json(name = "track_number") val trackNumber: Int,
        @Json(name = "type") val type: String,
        @Json(name = "uri") val uri: String
    ) {

      data class ExternalUrls(
          @Json(name = "spotify") val spotify: String
      )


      data class ExternalIds(
          @Json(name = "isrc") val isrc: String
      )


      data class Album(
          @Json(name = "album_type") val albumType: String,
          @Json(name = "artists") val artists: List<Artist>,
          @Json(name = "available_markets") val availableMarkets: List<String>,
          @Json(name = "external_urls") val externalUrls: ExternalUrls,
          @Json(name = "href") val href: String,
          @Json(name = "id") val id: String,
          @Json(name = "images") val images: List<Image>,
          @Json(name = "name") val name: String,
          @Json(name = "release_date") val releaseDate: String,
          @Json(name = "release_date_precision") val releaseDatePrecision: String,
          @Json(name = "total_tracks") val totalTracks: Int,
          @Json(name = "type") val type: String,
          @Json(name = "uri") val uri: String
      ) {

        data class ExternalUrls(
            @Json(name = "spotify") val spotify: String
        )


        data class Image(
            @Json(name = "height") val height: Int,
            @Json(name = "url") val url: String,
            @Json(name = "width") val width: Int
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
}