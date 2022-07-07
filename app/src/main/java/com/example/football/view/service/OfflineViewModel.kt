package com.example.football.view.service

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.data.remote.NewsRemote
import com.example.football.repository.NewsRepositoryImpl

class OfflineViewModel : ViewModel() {

    private val _recyclerListNews : MutableLiveData<HomeBaoMoiData> = MutableLiveData()
    private val recyclerListNews : LiveData<HomeBaoMoiData> = _recyclerListNews


    fun getListNewsObservable() : LiveData<HomeBaoMoiData> {
        return recyclerListNews
    }

    fun getListNews(){
        NewsRemote.loadListNews(recyclerListNews as MutableLiveData<HomeBaoMoiData>)
    }

}