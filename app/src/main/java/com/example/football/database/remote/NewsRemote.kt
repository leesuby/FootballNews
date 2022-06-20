package com.example.football.database.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.football.database.local.NewsLocal
import com.example.football.database.model.HomeBaoMoiData
import com.example.football.database.model.detail.DetailBaoMoiData
import com.example.football.database.model.home.ContentDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRemote {
    companion object{

        fun loadListNews(contentDao : ContentDao,data : MutableLiveData<HomeBaoMoiData>){
            val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
            val call = retroInstance.getNewsList()
            call.enqueue(object : Callback<HomeBaoMoiData> {
                override fun onFailure(call: Call<HomeBaoMoiData>, t: Throwable) {
                    data.postValue(null)
                }

                override fun onResponse(call: Call<HomeBaoMoiData>, response: Response<HomeBaoMoiData>) {
                    data.postValue(response.body())


                    //New background thread for save data to local
                    GlobalScope.launch(Dispatchers.IO){
                        NewsLocal.saveListNews(contentDao,data)
                    }

                }
            })
        }

        fun loadContentNews(data : MutableLiveData<DetailBaoMoiData>, id: Int){
            val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
            val call = retroInstance.getDetailNew(id)
            call.enqueue(object : Callback<DetailBaoMoiData> {
                override fun onFailure(call: Call<DetailBaoMoiData>, t: Throwable) {
                    data.postValue(null)
                }
                override fun onResponse(call: Call<DetailBaoMoiData>, response: Response<DetailBaoMoiData>) {
                    data.postValue(response.body())
                }
            })
        }
    }
}