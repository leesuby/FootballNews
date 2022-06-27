@file:OptIn(DelicateCoroutinesApi::class)

package com.example.football.view.service

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.football.data.local.NewsLocal
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.utils.Helpers

import com.example.football.viewmodel.NewsViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OfflineService : LifecycleService(){
    private var newsViewModel  = OfflineViewModel()


    override fun onCreate() {
        super.onCreate()

        GlobalScope.launch(Dispatchers.IO) {
            newsViewModel.getListNews(this@OfflineService,true)
        }

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        newsViewModel.getListNewsObservable().observe(this, Observer<HomeBaoMoiData>{
            if (it == null){

            }else{
                val list = MutableLiveData<HomeBaoMoiData>()
                list.postValue(it)
                Log.e("error",it.toString())
                GlobalScope.launch(Dispatchers.IO) {
                    NewsLocal.saveData(list)
                }

            }
        })
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        stopSelf()
        super.onDestroy()
    }
}