package com.example.football.data.remote

import com.example.football.data.model.detail.DetailBaoMoiData
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.data.model.home.CompetitionHomeBaoMoiData
import com.example.football.data.model.home.MatchHomeBaoMoiData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

//Service from retro to load data
interface RetroService {

    //Get list news data by page
    @GET("contents/home")
    @Headers("api_key: bm_fresher_2022")
    fun getNewsList(
        @Query("start") start: Int,
        @Query("size") size: Int
    ): Call<HomeBaoMoiData>


    //Get detail of news
    @GET("contents/detail")
    @Headers("api_key: bm_fresher_2022")
    fun getDetailNew(@Query("content_id") id: Int): Call<DetailBaoMoiData>


    //Get list match
    @GET("matches/by-date")
    @Headers("api_key: bm_fresher_2022")
    fun getMatchByDates(
        @Query("competition_id") id: Int,
        @Query("date") date: Int,
        @Query("start") start: Int,
        @Query("size") size: Int
    ): Call<MatchHomeBaoMoiData>


    //Get list competition
    @GET("competitions/hot")
    @Headers("api_key: bm_fresher_2022")
    fun getCompetitions() : Call<CompetitionHomeBaoMoiData>
}