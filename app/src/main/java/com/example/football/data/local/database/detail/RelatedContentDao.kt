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
            " JOIN contenthome ON contentrelated.relatedId = contenthome.contentId" +
            " WHERE contentrelated.contentId = :id  ")
    fun readAll(id: Int) : LiveData<HomeContent>

    @Query("SELECT contentId , title , date , publisherLogo, avatar, publisherLogoURL, avatarURL  " +
            " FROM contentrelated" +
            " WHERE contentrelated.relatedId = :id  ")
    suspend fun readAllSynchronous(id: Int) : List<HomeContent>
}