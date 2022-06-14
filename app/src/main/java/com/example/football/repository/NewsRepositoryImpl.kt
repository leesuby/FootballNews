package com.example.football.repository

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.football.database.remote.NewsRemote
import com.example.football.database.remote.RetroInstance
import com.example.football.database.remote.RetroService
import com.example.football.model.HomeBaoMoiData
import com.example.football.model.detail.DetailBaoMoiData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepositoryImpl : NewsRepository {

    override fun getListNews(data :MutableLiveData<HomeBaoMoiData>,context: Context?) {

        if (isNetworkAvailable(context)){
            NewsRemote.loadListNews(data)
        }
        else{
            Toast.makeText(context,"No Internet",Toast.LENGTH_SHORT).show()
        }

    }

    override fun getDetailNews(data : MutableLiveData<DetailBaoMoiData>,id: Int,context: Context?) {

        if (isNetworkAvailable(context)){
            NewsRemote.loadContentNews(data,id)
        }
        else{

        }


    }


    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}