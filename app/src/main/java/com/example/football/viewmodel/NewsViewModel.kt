package com.example.football.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.football.database.model.BaoMoiDatabase
import com.example.football.database.model.HomeBaoMoiData
import com.example.football.database.model.home.ContentDao
import com.example.football.repository.NewsRepositoryImpl

class NewsViewModel : ViewModel() {

    private val _recyclerListNews : MutableLiveData<HomeBaoMoiData> = MutableLiveData()
    private val recyclerListNews : LiveData<HomeBaoMoiData> = _recyclerListNews
    var contentDao : ContentDao = BaoMoiDatabase.getDatabase().ContentDao()
    var repo : NewsRepositoryImpl = NewsRepositoryImpl(contentDao)

    fun getListNewsObservable() : LiveData<HomeBaoMoiData>{
        return recyclerListNews
    }

    suspend fun getListNews(context: Context?){
        repo.getListNews(recyclerListNews as MutableLiveData,context)
    }

}