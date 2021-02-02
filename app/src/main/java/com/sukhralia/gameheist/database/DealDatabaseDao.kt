package com.sukhralia.gameheist.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sukhralia.gameheist.models.DealModel

@Dao
interface DealDatabaseDao {

    @Insert
    fun insert(dealModel : DealModel)

    @Insert
    fun insertAll(dealModel : List<DealModel>)

    @Update
    fun update(dealModel : DealModel)

    @Query("SELECT * FROM deal_table WHERE id = :key")
    fun get(key : Long) : DealModel

    @Delete
    fun delete(list : List<DealModel>)

    @Query("DELETE FROM deal_table")
    fun clear()

    @Query("SELECT * FROM deal_table ORDER BY id DESC")
    fun getAllDeals() : List<DealModel>

    @Query("SELECT * FROM deal_table ORDER BY id DESC LIMIT 1")
    fun getRecentDeal() : DealModel?

}