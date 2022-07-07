@file:OptIn(DelicateCoroutinesApi::class)

package com.example.football.view.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
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

    private val binder = OfflineBinder()

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class OfflineBinder : Binder(){
        fun getService(): OfflineService = this@OfflineService
    }

    private var newsViewModel  = OfflineViewModel()

    override fun onCreate() {
        super.onCreate()

        GlobalScope.launch(Dispatchers.IO) {
            newsViewModel.getListNews()
        }

    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)

        newsViewModel.getListNewsObservable().observe(this, Observer<HomeBaoMoiData>{
            if (it == null){

            }else{
                val list = MutableLiveData<HomeBaoMoiData>()
                list.postValue(it)
                saveData(list)

            }
        })

        return binder
    }

    fun saveData(list : MutableLiveData<HomeBaoMoiData>){
        GlobalScope.launch(Dispatchers.IO) {
            NewsLocal.saveData(list)
        }
    }

}