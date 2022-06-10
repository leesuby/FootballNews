package com.example.football.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.football.database.RetroInstance
import com.example.football.database.RetroService
import com.example.football.model.HomeBaoMoiData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {

    var  recyclerListNews : MutableLiveData<HomeBaoMoiData>

    init{
        recyclerListNews = MutableLiveData()
    }

    fun getListNewsObservable() : MutableLiveData<HomeBaoMoiData>{
        return recyclerListNews
    }


    fun getListNews(){
        val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.getNewsList()
        call.enqueue(object : Callback<HomeBaoMoiData>{
            override fun onFailure(call: Call<HomeBaoMoiData>, t: Throwable) {
                recyclerListNews.postValue(null)
            }

            override fun onResponse(call: Call<HomeBaoMoiData>, response: Response<HomeBaoMoiData>) {
                recyclerListNews.postValue(response.body())
            }
        })

    }

}