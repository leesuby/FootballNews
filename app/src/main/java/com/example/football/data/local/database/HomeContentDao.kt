package com.example.football.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.football.data.model.Content

@Dao
interface HomeContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addContent(homeContent: HomeContent)

    @Query("SELECT * FROM ContentHome ORDER BY content_id DESC")
    fun readAllContent() : LiveData<HomeContent>

    @Query("SELECT * FROM ContentHome ORDER BY date DESC")
    suspend fun readAllContentSynchronous() : List<HomeContent>
}