package com.example.football.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.football.data.local.database.BaoMoiDatabase
import com.example.football.data.model.detail.DetailBaoMoiData

import com.example.football.repository.NewsRepositoryImpl

class DetailsViewModel : ViewModel() {

    private val  _DetailNew : MutableLiveData<DetailBaoMoiData> = MutableLiveData()
    private val DetailNew : LiveData<DetailBaoMoiData> = _DetailNew
    var repo : NewsRepositoryImpl = NewsRepositoryImpl()


    fun getDetailNewObservable() : LiveData<DetailBaoMoiData> {
        return DetailNew
    }


    fun getDetailNew(id: Int,context: Context?){
        repo.getDetailNews(DetailNew as MutableLiveData<DetailBaoMoiData>,id,context)
    }

}