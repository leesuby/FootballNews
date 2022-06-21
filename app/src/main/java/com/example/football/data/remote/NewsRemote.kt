package com.example.football.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.football.data.local.NewsLocal
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.data.model.detail.DetailBaoMoiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRemote {
    companion object{

        //request API get list new data
        fun loadListNews(data : MutableLiveData<HomeBaoMoiData>){
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
                        NewsLocal.saveData(data)
                    }

                }
            })
        }

        fun loadContentNews(data : MutableLiveData<DetailBaoMoiData>, id: Int,isSave: Boolean = false){
            val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
            val call = retroInstance.getDetailNew(id)
            call.enqueue(object : Callback<DetailBaoMoiData> {
                override fun onFailure(call: Call<DetailBaoMoiData>, t: Throwable) {
                    data.postValue(null)
                }
                override fun onResponse(call: Call<DetailBaoMoiData>, response: Response<DetailBaoMoiData>) {
                    data.postValue(response.body())

                    if(isSave){
                        GlobalScope.launch(Dispatchers.IO){
                            NewsLocal.saveDetailContentNews(data)
                        }
                    }
                }
            })
        }
    }
}