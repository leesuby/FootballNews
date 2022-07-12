package com.example.football.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.football.data.local.database.detail.*
import com.example.football.data.local.database.home.HomeContent
import com.example.football.data.local.database.home.HomeContentDao
import com.example.football.utils.Helpers
import com.example.football.utils.MainApplication

@Database(entities = [
    HomeContent::class,
    DetailContent::class,
    BodyDetailContent::class,
    RelatedContent::class
                     ], version = 1)

@TypeConverters(Helpers::class)

abstract class BaoMoiDatabase : RoomDatabase(){

    abstract fun HomeContentDao() : HomeContentDao
    abstract fun DetailContentDao() : DetailContentDao
    abstract fun BodyDetailContentDao() : BodyDetailContentDao
    abstract fun RelatedContentDao() : RelatedContentDao

    companion object{

        @Volatile
        private var INSTANCE : BaoMoiDatabase? = null

        fun getDatabase() : BaoMoiDatabase {
            val tempInstant = INSTANCE
            if (tempInstant != null )
                return tempInstant
            synchronized(this){
                val instance = Room.databaseBuilder(
                    MainApplication.applicationContext(),
                    BaoMoiDatabase::class.java,
                    "baomoi_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}