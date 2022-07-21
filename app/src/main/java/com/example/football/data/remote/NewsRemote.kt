package com.example.football.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.football.data.local.NewsLocal
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.data.model.detail.DetailBaoMoiData
import com.example.football.data.model.home.CompetitionHomeBaoMoiData
import com.example.football.data.model.home.MatchHomeBaoMoiData
import com.example.football.utils.Converter
import com.example.football.utils.Global
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NewsRemote {

        //Request API get list new data
        @OptIn(DelicateCoroutinesApi::class)
        fun loadListNews(data : MutableLiveData<HomeBaoMoiData>, page: Int = 0, loadOnline: Boolean= false){
            //Create retro instance to load data
            val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
            val call = retroInstance.getNewsList(page*20,20)
            call.enqueue(object : Callback<HomeBaoMoiData> {

                //Fail data
                override fun onFailure(call: Call<HomeBaoMoiData>, t: Throwable) {
                    data.postValue(null)
                }

                //Success data
                override fun onResponse(call: Call<HomeBaoMoiData>, response: Response<HomeBaoMoiData>) {
                    //If using this function for loading page on Home
                    if(loadOnline)
                    {
                        GlobalScope.launch(Dispatchers.IO) {
                            //Get bitmap from URL for custom view
                            for(content in response.body()?.data!!.contents){
                                content.bitmapAvatar = Converter.loadBitmapFromUrl(content.avatarUrl)
                                content.bitmapLogo= Converter.loadBitmapFromUrl(content.publisherLogo)
                            }

                            data.postValue(response.body())
                        }

                    }
                    else
                    {
                        data.postValue(response.body())

                    }

                }
            })
        }

        //Request API to load detail news ( content of news)
        @OptIn(DelicateCoroutinesApi::class)
        fun loadContentNews(data : MutableLiveData<DetailBaoMoiData>, id: Int, isSave: Boolean = false){
            //Create retro instance to load data
            val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
            val call = retroInstance.getDetailNew(id)

            call.enqueue(object : Callback<DetailBaoMoiData> {

                //Fail data
                override fun onFailure(call: Call<DetailBaoMoiData>, t: Throwable) {
                    data.postValue(null)
                }

                //Success data
                override fun onResponse(call: Call<DetailBaoMoiData>, response: Response<DetailBaoMoiData>) {
                    //Load from UI thread for better performance
                    GlobalScope.launch(Dispatchers.IO) {
                        //Get bitmap from URL for custom view
                        for(related in response.body()?.data?.related?.contents!!){
                            if(related.avatarUrl!=null)
                                related.bitmapAvatar = Converter.loadBitmapFromUrl(related.avatarUrl)
                            if(related.publisherLogo!=null)
                                related.bitmapLogo = Converter.loadBitmapFromUrl(related.publisherLogo)
                        }

                        data.postValue(response.body())

                        //If user have offline mode, save data to local for the next time
                        if(Global.isOfflineMode){
                            //If is save mode
                            if(isSave){
                                GlobalScope.launch(Dispatchers.IO){
                                    NewsLocal.saveDetailContentNews(data)
                                }
                            }}

                    }




                }
            })
        }

        //Request API to get list match on Home page
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

        //Request API to get list competition on Home page
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