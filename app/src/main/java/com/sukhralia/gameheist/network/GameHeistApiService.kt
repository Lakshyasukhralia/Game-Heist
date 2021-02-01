package com.sukhralia.gameheist.network

import androidx.datastore.preferences.createDataStore
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.sukhralia.gameheist.BuildConfig
import com.sukhralia.gameheist.models.DealModel
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url

private const val BASE_URL = BuildConfig.APP_BASE_URL

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface GameHeistApiService {

    @GET("giveaways")
    fun getGiveAwayAsync(@QueryMap options : Map<String, String>):
            Deferred<List<DealModel>>
}

object GameHeistApi {
    val retrofitService: GameHeistApiService by lazy {
        retrofit.create(GameHeistApiService::class.java)
    }
}
