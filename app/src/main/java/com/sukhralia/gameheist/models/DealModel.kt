package com.sukhralia.gameheist.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DealModel(
    @Json(name = "description") val description: String,
    @Json(name = "end_date") val end_date: String,
    @Json(name = "gamerpower_url") val gamerpower_url: String,
    @Json(name = "id") val id: Int,
    @Json(name = "image") val image: String,
    @Json(name = "instructions") val instructions: String,
    @Json(name = "open_giveaway") val open_giveaway: String,
    @Json(name = "open_giveaway_url") val open_giveaway_url: String,
    @Json(name = "platforms") val platforms: String,
    @Json(name = "status") val status: String,
    @Json(name = "thumbnail") val thumbnail: String,
    @Json(name = "title") val title: String,
    @Json(name = "type") val type: String,
    @Json(name = "users") val users: Int,
    @Json(name = "worth") val worth: String
): Parcelable