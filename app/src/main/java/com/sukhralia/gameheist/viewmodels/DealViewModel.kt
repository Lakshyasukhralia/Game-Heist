package com.sukhralia.gameheist.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukhralia.gameheist.database.DealDatabaseDao
import com.sukhralia.gameheist.models.DealModel
import com.sukhralia.gameheist.network.GameHeistApi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlin.math.min

@ExperimentalCoroutinesApi
class DealViewModel(val database: DealDatabaseDao, application: Application) :
    AndroidViewModel(application) {

    val response = MutableStateFlow<ResponseState>(ResponseState.Empty)

    lateinit var mPlt: String
    lateinit var mType: String
    lateinit var mSort: String

    var checkDb = true

    fun getDeals(plt: String = mPlt, type: String = mType, sort: String = mSort) {

        mPlt = plt
        mType = type
        mSort = sort

        viewModelScope.launch(Dispatchers.IO) {
            response.value = ResponseState.Loading
            fetchResult()
        }
    }

    private suspend fun fetchResult() {
        val query = HashMap<String, String>()

        if (mPlt != "any")
            query["platform"] = mPlt
        if (mType != "any")
            query["type"] = mType
        if (mSort != "any")
            query["sort-by"] = mSort

        val getDealsDeferred = GameHeistApi.retrofitService.getGiveAwayAsync(query)

        try {
            val dealsResult = getDealsDeferred.await()
            response.value = ResponseState.Success(dealsResult)
        } catch (t: Throwable) {
            response.value = ResponseState.Error(t.toString())
        }
    }

    suspend fun checkNewDeals() {
        checkDb = false
        response.collect { newData ->
            when (newData) {
                is ResponseState.Success -> {

                    var counter = 0

                    val a = newData.data
                    val b = database.getAllDeals()

                    for (item in a) {
                        for (item2 in b) {
                            if (item.id == item2.id)
                                counter++
                        }
                    }

                    var newDeals = b - counter

                    database.clear()
                    database.insertAll(newData.data)
                }
                else -> {
                }
            }
        }
    }

    sealed class ResponseState {
        data class Success(val data: List<DealModel>) : ResponseState()
        data class Error(val error: String) : ResponseState()
        object Loading : ResponseState()
        object Empty : ResponseState()
    }
}