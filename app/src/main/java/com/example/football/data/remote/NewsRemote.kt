package com.example.football.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.football.data.local.NewsLocal
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.data.model.detail.DetailBaoMoiData
import com.example.football.data.model.home.CompetitionHomeBaoMoiData
import com.example.football.data.model.home.MatchHomeBaoMoiData
import com.example.football.utils.Helpers
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class NewsRemote {
    companion object{

        //request API get list new data
        @OptIn(DelicateCoroutinesApi::class)
        fun loadListNews(data : MutableLiveData<HomeBaoMoiData>, page: Int = 0, loadOnline: Boolean= false){
            val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
            val call = retroInstance.getNewsList(page*20,20)
            call.enqueue(object : Callback<HomeBaoMoiData> {
                override fun onFailure(call: Call<HomeBaoMoiData>, t: Throwable) {
                    data.postValue(null)
                }

                override fun onResponse(call: Call<HomeBaoMoiData>, response: Response<HomeBaoMoiData>) {
                    if(loadOnline)
                    {
                        GlobalScope.launch(Dispatchers.IO) {
                            for(content in response.body()?.data!!.contents){
                                content.bitmapAvatar = Helpers.mLoad(content.avatar_url)
                                content.bitmapLogo= Helpers.mLoad(content.publisher_logo)
                            }
                            Log.e("respone",response.body().toString())
                            data.postValue(response.body())
                        }

                    }
                    else
                        data.postValue(response.body())

                }
            })
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun loadContentNews(data : MutableLiveData<DetailBaoMoiData>, id: Int, isSave: Boolean = false){
            val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
            val call = retroInstance.getDetailNew(id)
            call.enqueue(object : Callback<DetailBaoMoiData> {
                override fun onFailure(call: Call<DetailBaoMoiData>, t: Throwable) {
                    data.postValue(null)
                }
                override fun onResponse(call: Call<DetailBaoMoiData>, response: Response<DetailBaoMoiData>) {
                    data.postValue(response.body())

                    if(Helpers.isOfflineMode){
                    if(isSave){
                        GlobalScope.launch(Dispatchers.IO){
                            NewsLocal.saveDetailContentNews(data)
                        }
                    }}
                }
            })
        }

        fun loadListMatchHome(data : MutableLiveData<MatchHomeBaoMoiData>){
            val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
            val call = retroInstance.getMatchByDates(0,0,0,20)
            call.enqueue(object : Callback<MatchHomeBaoMoiData> {
                override fun onFailure(call: Call<MatchHomeBaoMoiData>, t: Throwable) {
                    data.postValue(null)
                }

                override fun onResponse(call: Call<MatchHomeBaoMoiData>, response: Response<MatchHomeBaoMoiData>) {
                    data.postValue(response.body())
                }
            })
        }

        fun loadCompetitionHome(data : MutableLiveData<CompetitionHomeBaoMoiData>){
            val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
            val call = retroInstance.getCompetitions()
            call.enqueue(object : Callback<CompetitionHomeBaoMoiData> {
                override fun onFailure(call: Call<CompetitionHomeBaoMoiData>, t: Throwable) {
                    data.postValue(null)
                }

                override fun onResponse(call: Call<CompetitionHomeBaoMoiData>, response: Response<CompetitionHomeBaoMoiData>) {
                    data.postValue(response.body())
                }
            })
        }
    }
}