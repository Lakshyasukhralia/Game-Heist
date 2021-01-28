package com.sukhralia.gameheist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukhralia.gameheist.models.DealModel
import com.sukhralia.gameheist.network.GameRiftApi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
class DealViewModel : ViewModel() {

    val response = MutableStateFlow<ResponseState>(ResponseState.Empty)

    lateinit var mPlt : String
    lateinit var mType : String
    lateinit var mSort : String

    fun getDeals(plt : String = mPlt, type: String = mType, sort: String = mSort) {

        mPlt = plt
        mType = type
        mSort = sort

        viewModelScope.launch(Dispatchers.IO) {
            response.value = ResponseState.Loading
            getResult()
        }
    }

    private suspend fun getResult() {

        val query = HashMap<String,String>()

        if(mPlt!="any")
            query["platform"] = mPlt
        if(mType!="any")
            query["type"] = mType
        if(mSort!="any")
            query["sort-by"] = mSort

        val getDealsDeferred = GameRiftApi.retrofitService.getGiveAwayAsync(query)

        try {
            val dealsResult = getDealsDeferred.await()
            response.value = ResponseState.Success(dealsResult)
        } catch (t: Throwable) {
            response.value = ResponseState.Error(t.toString())
        }

    }

    sealed class ResponseState {
        data class Success(val data: List<DealModel>) : ResponseState()
        data class Error(val error: String) : ResponseState()
        object Loading : ResponseState()
        object Empty : ResponseState()
    }
}