package com.example.football.data.local.database.detail

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.football.data.local.database.home.HomeContent

@Dao
interface RelatedContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRelatedContent(relatedContent: RelatedContent)

    @Query("SELECT contenthome.* " +
            "FROM contentrelated" +
            " JOIN contenthome ON contentrelated.related_id = contenthome.content_id" +
            " WHERE contentrelated.content_id = :id  ")
    fun readAll(id: Int) : LiveData<HomeContent>

    @Query("SELECT content_id , title , date , publisher_logo, avatar,publisher_logo_URL,avatar_URL  " +
            " FROM contentrelated" +
            " WHERE contentrelated.related_id = :id  ")
    suspend fun readAllSynchronous(id: Int) : List<HomeContent>
}