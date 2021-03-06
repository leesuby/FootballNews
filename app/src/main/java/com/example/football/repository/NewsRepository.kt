package com.example.football.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.data.model.detail.DetailBaoMoiData
import com.example.football.data.model.home.CompetitionHomeBaoMoiData
import com.example.football.data.model.home.MatchHomeBaoMoiData

interface NewsRepository  {

    //get list news to show on Home
    fun getListNews(data : MutableLiveData<HomeBaoMoiData>, context: Context?, loadToSave: Boolean = false,page : Int)

    //get content of a new to show on detail news
    fun getDetailNews(data : MutableLiveData<DetailBaoMoiData>,id: Int,context: Context?)

    //get match to show on Home
    fun getListMatchNews(data : MutableLiveData<MatchHomeBaoMoiData>)

    //get competition to show on Home
    fun getListCompetitionNews(data: MutableLiveData<CompetitionHomeBaoMoiData>)

}