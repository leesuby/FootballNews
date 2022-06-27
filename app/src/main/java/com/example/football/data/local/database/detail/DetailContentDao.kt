package com.example.football.data.local.database.detail


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface DetailContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDetailContent(detailContent: DetailContent)

    @Query("SELECT * FROM ContentDetail")
    fun readAll() : LiveData<DetailContent>

    @Query("SELECT * FROM ContentDetail WHERE content_id = :id ")
    suspend fun readAllSynchronous(id: Int) : List<DetailContent>

    @Query("SELECT EXISTS(SELECT * FROM contentdetail WHERE content_id = :id)")
    fun isRowIsExist(id : Int) : Boolean
}