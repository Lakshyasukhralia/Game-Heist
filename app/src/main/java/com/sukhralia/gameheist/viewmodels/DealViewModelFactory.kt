package com.sukhralia.gameheist.viewmodels

//import android.app.Application
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.sukhralia.gameheist.database.DealDatabaseDao
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//
//class DealViewModelFactory(private val database : DealDatabaseDao,private val application: Application) : ViewModelProvider.Factory  {
//
//    @ExperimentalCoroutinesApi
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(DealViewModel::class.java)) {
//            return DealViewModel(
//                database, application
//            ) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//
//}