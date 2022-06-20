package com.example.football.database.model.home

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.football.database.model.Content

@Dao
interface ContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addContent(content: Content)

    @Query("SELECT * FROM contenthome ORDER BY content_id DESC")
    fun readAllContent() : LiveData<Content>

    @Query("SELECT * FROM contenthome ORDER BY content_id DESC")
    suspend fun readAllContentSynchronous() : List<Content>
}