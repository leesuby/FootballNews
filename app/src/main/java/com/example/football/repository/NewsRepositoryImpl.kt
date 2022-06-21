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

    override suspend fun getListNews(data : MutableLiveData<HomeBaoMoiData>, context: Context?) {


        Helpers.internet = isNetworkAvailable(context)
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

        //online mode
        Helpers.internet = isNetworkAvailable(context)
        if (isNetworkAvailable(context)){
            NewsRemote.loadContentNews(data,id)
        }
        else{
            //offline mode

        }


    }


    private fun isNetworkAvailable(context: Context?): Boolean {
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