package com.example.football.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.football.model.detail.DetailBaoMoiData
import com.example.football.repository.NewsRepositoryImpl

class DetailsViewModel : ViewModel() {

    var  DetailNew : MutableLiveData<DetailBaoMoiData> = MutableLiveData()
    var  repo : NewsRepositoryImpl = NewsRepositoryImpl()

    fun getDetailNewObservable() : MutableLiveData<DetailBaoMoiData> {
        return DetailNew
    }


    fun getDetailNew(id: Int){
        repo.getDetailNews(DetailNew,id)
    }

}