package com.example.football.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.football.data.local.NewsLocal
import com.example.football.data.local.database.BaoMoiDatabase
import com.example.football.data.local.database.detail.DetailContentDao
import com.example.football.data.remote.NewsRemote
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.data.model.detail.DetailBaoMoiData
import com.example.football.data.model.home.CompetitionHomeBaoMoiData
import com.example.football.data.model.home.MatchHomeBaoMoiData
import com.example.football.utils.Global
import com.example.football.utils.Helpers

class NewsRepositoryImpl() : NewsRepository {
    var detailContentDao : DetailContentDao = BaoMoiDatabase.getDatabase().DetailContentDao()

    override fun getListNews(data : MutableLiveData<HomeBaoMoiData>, context: Context?, loadToSave : Boolean, page: Int) {
        if (Global.internet){ //online mode
            //load data from database for faster experience for initialize after splash screen
            if(Global.isOfflineMode && !loadToSave)
                NewsLocal.loadListNewsByPage(data,0)
            else
                NewsRemote.loadListNews(data,page = page,loadToSave)
        }

        else{
            //offline mode
            NewsLocal.loadListNewsAll(data)
        }

    }



    override fun getDetailNews(data : MutableLiveData<DetailBaoMoiData>,id: Int,context: Context?) {
        if (Global.internet){//Online mode
            //load data from local if there is data on local
            if(detailContentDao.isRowIsExist(id)){
                NewsLocal.loadDetailContent(data,id)
            }
            else{
                //load data by request API
                NewsRemote.loadContentNews(data,id)
            }
        }
        else{
            //offline mode
            NewsLocal.loadDetailContent(data,id)
        }
    }

    override fun getListMatchNews(data : MutableLiveData<MatchHomeBaoMoiData>){
        if (Global.internet){ //online mode
            NewsRemote.loadListMatchHome(data)
        }
    }

    override fun getListCompetitionNews(data: MutableLiveData<CompetitionHomeBaoMoiData>) {
        if (Global.internet){ //online mode
            NewsRemote.loadCompetitionHome(data)
        }
    }
}