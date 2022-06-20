package com.example.football.database.remote

import com.example.football.database.model.detail.DetailBaoMoiData
import com.example.football.database.model.HomeBaoMoiData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetroService {

    @GET("contents/home")
    @Headers("api_key: bm_fresher_2022")
    fun getNewsList() : Call<HomeBaoMoiData>

    @GET("contents/detail")
    @Headers("api_key: bm_fresher_2022")
    fun getDetailNew(@Query("content_id") id : Int) : Call<DetailBaoMoiData>
}