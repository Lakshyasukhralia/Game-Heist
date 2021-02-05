package com.sukhralia.gameheist.network

import com.sukhralia.gameheist.BuildConfig.APP_BASE_URL
import com.sukhralia.gameheist.models.DealModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface GameHeistApiService {

    @GET(APP_BASE_URL + "giveaways")
    fun fetchDealsAsync(@QueryMap options : Map<String, String>):
            Deferred<List<DealModel>>
}
