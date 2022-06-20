package com.example.football.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.football.database.model.BaoMoiDatabase
import com.example.football.database.model.detail.DetailBaoMoiData
import com.example.football.database.model.home.ContentDao
import com.example.football.repository.NewsRepositoryImpl

class DetailsViewModel : ViewModel() {

    private val  _DetailNew : MutableLiveData<DetailBaoMoiData> = MutableLiveData()
    private val DetailNew : LiveData<DetailBaoMoiData> = _DetailNew
    var contentDao : ContentDao = BaoMoiDatabase.getDatabase().ContentDao()
    var repo : NewsRepositoryImpl = NewsRepositoryImpl(contentDao)


    fun getDetailNewObservable() : LiveData<DetailBaoMoiData> {
        return DetailNew
    }


    fun getDetailNew(id: Int,context: Context?){
        repo.getDetailNews(DetailNew as MutableLiveData<DetailBaoMoiData>,id,context)
    }

}