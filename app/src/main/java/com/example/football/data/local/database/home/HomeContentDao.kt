package com.example.football.data.local.database.home

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.football.data.local.database.home.HomeContent

@Dao
interface HomeContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addContent(homeContent: HomeContent)

    @Query("SELECT * FROM ContentHome ORDER BY content_id DESC")
    fun readAllContent() : LiveData<HomeContent>

    @Query("SELECT * " +
            "FROM ContentHome " +
            "ORDER BY date DESC " +
            "LIMIT 20 " +
            "OFFSET :page * 20")
    suspend fun readAllContentSynchronousByPage(page : Int) : List<HomeContent>

    @Query("SELECT * " +
            "FROM ContentHome " +
            "ORDER BY date DESC")
    suspend fun readAllContentSynchronous() : List<HomeContent>
}