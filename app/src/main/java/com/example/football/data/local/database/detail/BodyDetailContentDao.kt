package com.example.football.data.local.database.detail

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BodyDetailContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBodyDetailContent(bodyDetailContent: BodyDetailContent)

    @Query("SELECT * FROM DetailBody WHERE contentId = :id ")
    fun readAll(id: Int) : LiveData<BodyDetailContent>

    @Query("SELECT * FROM DetailBody WHERE contentId = :id ")
    suspend fun readAllSynchronous(id: Int) : List<BodyDetailContent>
}