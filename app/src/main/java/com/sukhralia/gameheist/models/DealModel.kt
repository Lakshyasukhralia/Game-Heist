package com.sukhralia.gameheist.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Parcelize
@Entity(tableName = "deal_table")
data class DealModel(
    @PrimaryKey(autoGenerate = true)
    var uId : Long = 0,
    @ColumnInfo(name = "description")
    @Json(name = "description") val description: String,
    @ColumnInfo(name = "end_date")
    @Json(name = "end_date") val end_date: String,
    @ColumnInfo(name = "gamerpower_url")
    @Json(name = "gamerpower_url") val gamerpower_url: String,
    @ColumnInfo(name = "id")
    @Json(name = "id") val id: Int,
    @ColumnInfo(name = "image")
    @Json(name = "image") val image: String,
    @ColumnInfo(name = "instructions")
    @Json(name = "instructions") val instructions: String,
    @ColumnInfo(name = "open_giveaway")
    @Json(name = "open_giveaway") val open_giveaway: String,
    @ColumnInfo(name = "open_giveaway_url")
    @Json(name = "open_giveaway_url") val open_giveaway_url: String,
    @ColumnInfo(name = "platforms")
    @Json(name = "platforms") val platforms: String,
    @ColumnInfo(name = "status")
    @Json(name = "status") val status: String,
    @ColumnInfo(name = "thumbnail")
    @Json(name = "thumbnail") val thumbnail: String,
    @ColumnInfo(name = "title")
    @Json(name = "title") val title: String,
    @ColumnInfo(name = "type")
    @Json(name = "type") val type: String,
    @ColumnInfo(name = "users")
    @Json(name = "users") val users: Int,
    @ColumnInfo(name = "worth")
    @Json(name = "worth") val worth: String
): Parcelable
