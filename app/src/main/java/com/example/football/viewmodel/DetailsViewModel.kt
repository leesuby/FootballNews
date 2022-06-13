package com.example.football.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.football.database.RetroInstance
import com.example.football.database.RetroService
import com.example.football.model.detail.DetailBaoMoiData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel : ViewModel() {
    var  DetailNew : MutableLiveData<DetailBaoMoiData> = MutableLiveData()

    fun getDetailNewObservable() : MutableLiveData<DetailBaoMoiData> {
        return DetailNew
    }


    fun getDetailNew(id: Int){
        val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.getDetailNew(id)
        call.enqueue(object : Callback<DetailBaoMoiData> {
            override fun onFailure(call: Call<DetailBaoMoiData>, t: Throwable) {
                DetailNew.postValue(null)
            }
            override fun onResponse(call: Call<DetailBaoMoiData>, response: Response<DetailBaoMoiData>) {
                DetailNew.postValue(response.body())
            }
        })

    }

}