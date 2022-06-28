package com.example.football.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.data.model.home.MatchHomeBaoMoiData
import com.example.football.repository.NewsRepositoryImpl

class NewsViewModel : ViewModel() {

    private val _recyclerListNews : MutableLiveData<HomeBaoMoiData> = MutableLiveData()
    private val recyclerListNews : LiveData<HomeBaoMoiData> = _recyclerListNews

    private val _recyclerListMatchNews : MutableLiveData<MatchHomeBaoMoiData> = MutableLiveData()
    private val recyclerListMatchNews : LiveData<MatchHomeBaoMoiData> = _recyclerListMatchNews

    var repo : NewsRepositoryImpl = NewsRepositoryImpl()

    fun getListNewsObservable() : LiveData<HomeBaoMoiData>{
        return recyclerListNews
    }

    fun getListNews(context: Context?){
        repo.getListNews(recyclerListNews as MutableLiveData, context,)
    }

    fun getListMatchObservable() : LiveData<MatchHomeBaoMoiData>{
        return recyclerListMatchNews
    }

    fun getListMatch(){
        repo.getListMatchNews(recyclerListMatchNews as MutableLiveData)
    }
}