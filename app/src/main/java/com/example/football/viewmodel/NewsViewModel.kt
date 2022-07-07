package com.example.football.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.football.data.model.Content
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.data.model.home.CompetitionHomeBaoMoiData
import com.example.football.data.model.home.MatchHomeBaoMoiData
import com.example.football.repository.NewsRepositoryImpl

class NewsViewModel : ViewModel() {
    private companion object{

        private val _recyclerListNews : MutableLiveData<HomeBaoMoiData> = MutableLiveData()
        private val _recyclerListMatchNews : MutableLiveData<MatchHomeBaoMoiData> = MutableLiveData()
        private val _recyclerListComepetitionNews : MutableLiveData<CompetitionHomeBaoMoiData> = MutableLiveData()

    }

    private val recyclerListNews : LiveData<HomeBaoMoiData> = _recyclerListNews

    private val recyclerListMatchNews : LiveData<MatchHomeBaoMoiData> = _recyclerListMatchNews

    private val recyclerListComepetitionNews : LiveData<CompetitionHomeBaoMoiData> = _recyclerListComepetitionNews

    var repo : NewsRepositoryImpl = NewsRepositoryImpl()

    fun getListNewsObservable() : LiveData<HomeBaoMoiData>{
        return recyclerListNews
    }

    fun getListNews(context: Context?,page: Int = 0,loadOnline: Boolean){
        repo.getListNews(recyclerListNews as MutableLiveData, context, page = page, loadOnline = loadOnline)
    }

    fun addListNews(tmp : List<Content>){
        recyclerListNews as MutableLiveData
        recyclerListNews.value!!.data.contents = recyclerListNews.value!!.data.contents + tmp
    }

    fun getListMatchObservable() : LiveData<MatchHomeBaoMoiData>{
        return recyclerListMatchNews
    }

    fun getListMatch(){
        repo.getListMatchNews(recyclerListMatchNews as MutableLiveData)
    }

    fun getListCompetitionObservable() : LiveData<CompetitionHomeBaoMoiData>{
        return recyclerListComepetitionNews
    }

    fun getListCompetition(){
        repo.getListCompetitionNews(recyclerListComepetitionNews as MutableLiveData)
    }
}