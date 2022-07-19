
package com.example.football.view.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.football.data.local.NewsLocal
import com.example.football.data.model.HomeBaoMoiData

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OfflineService : LifecycleService(){

    private val binder = OfflineBinder()

    //Class to get service
    inner class OfflineBinder : Binder(){
        fun getService(): OfflineService = this@OfflineService
    }

    //Viewmodel to get data from request API
    private var newsViewModel  = OfflineViewModel()

    override fun onCreate() {
        super.onCreate()

        //first initial to get data from online
        GlobalScope.launch(Dispatchers.IO) {
            newsViewModel.getListNews()
        }

    }


    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)

        //init observer
        observer()

        return binder
    }


    //create observer for viewmodel when data is request from URL
    fun observer(){
        newsViewModel.getListNewsObservable().observe(this) {
            if (it == null) {

            } else {
                val list = MutableLiveData<HomeBaoMoiData>()
                list.postValue(it)
                saveData(list)

            }
        }
    }


    //Save data to local
    fun saveData(list: MutableLiveData<HomeBaoMoiData>){
        GlobalScope.launch(Dispatchers.IO) {
            NewsLocal.saveData(list)
        }
    }

}