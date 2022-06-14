package com.example.football.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.football.model.HomeBaoMoiData
import com.example.football.repository.NewsRepositoryImpl

class NewsViewModel : ViewModel() {

    var recyclerListNews : MutableLiveData<HomeBaoMoiData> = MutableLiveData()
    var repo : NewsRepositoryImpl = NewsRepositoryImpl()

    fun getListNewsObservable() : MutableLiveData<HomeBaoMoiData>{
        return recyclerListNews
    }

    fun getListNews(context: Context?){
        repo.getListNews(recyclerListNews,context)
    }

}