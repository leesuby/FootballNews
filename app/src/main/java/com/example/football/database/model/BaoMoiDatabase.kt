package com.example.football.database.model

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.football.database.model.home.ContentDao
import com.example.football.utils.MainApplication

@Database(entities = [Content::class], version = 1)
abstract class BaoMoiDatabase : RoomDatabase(){

    abstract fun ContentDao() : ContentDao

    companion object{

        @Volatile
        private var INSTANCE : BaoMoiDatabase? = null

        fun getDatabase() : BaoMoiDatabase{
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