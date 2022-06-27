package com.example.football.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.football.data.local.NewsLocal
import com.example.football.data.remote.NewsRemote
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.data.model.detail.DetailBaoMoiData
import com.example.football.utils.Helpers

class NewsRepositoryImpl() : NewsRepository {

    override fun getListNews(data : MutableLiveData<HomeBaoMoiData>, context: Context?) {
        if (Helpers.internet){ //online mode
            //get data from request API and save to local database
            NewsRemote.loadListNews(data)
        }
        else{ //offline mode
            NewsLocal.loadListNews(data)
            Toast.makeText(context,"No Internet",Toast.LENGTH_SHORT).show()
        }

    }

    override fun getDetailNews(data : MutableLiveData<DetailBaoMoiData>,id: Int,context: Context?) {
        if (Helpers.internet){
            NewsRemote.loadContentNews(data,id)
        }
        else{
            //offline mode
            NewsLocal.loadDetailContent(data,id)
            Toast.makeText(context,"No Internet",Toast.LENGTH_SHORT).show()
        }


    }

}