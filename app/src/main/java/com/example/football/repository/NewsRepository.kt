package com.example.football.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.data.model.detail.DetailBaoMoiData

interface NewsRepository  {

    //get list news to show on Home
    suspend fun getListNews(data : MutableLiveData<HomeBaoMoiData>, context: Context?)

    //get content of a new to show on detail news
    fun getDetailNews(data : MutableLiveData<DetailBaoMoiData>,id: Int,context: Context?)
}