package com.example.football.repository

import androidx.lifecycle.MutableLiveData
import com.example.football.model.HomeBaoMoiData
import com.example.football.model.detail.DetailBaoMoiData

interface NewsRepository  {

    //get list news to show on Home
    fun getListNews(data :MutableLiveData<HomeBaoMoiData>)

    //get content of a new to show on detail news
    fun getDetailNews(data : MutableLiveData<DetailBaoMoiData>,id: Int)
}