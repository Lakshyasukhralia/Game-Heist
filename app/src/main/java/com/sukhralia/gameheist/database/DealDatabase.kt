package com.sukhralia.gameheist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sukhralia.gameheist.models.DealModel

@Database(entities = [DealModel::class], version = 2, exportSchema = false)
abstract class DealDatabase : RoomDatabase() {

    abstract val dealDatabaseDao: DealDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: DealDatabase? = null

        fun getInstance(context: Context): DealDatabase {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DealDatabase::class.java,
                        "deal_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }

}