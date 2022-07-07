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
import com.example.football.utils.Helpers

class NewsRepositoryImpl() : NewsRepository {
    var detailContentDao : DetailContentDao = BaoMoiDatabase.getDatabase().DetailContentDao()

    override fun getListNews(data : MutableLiveData<HomeBaoMoiData>, context: Context?, loadOnline : Boolean,page: Int) {
        if (Helpers.internet){ //online mode

            //load data from database for faster experience for initialize after splash screen
            if(Helpers.isOfflineMode && !loadOnline)
                NewsLocal.loadListNewsByPage(data,0)
            else
                NewsRemote.loadListNews(data,page = page)
        }

        else{
            //offline mode
            NewsLocal.loadListNewsAll(data)
            Toast.makeText(context,"No Internet",Toast.LENGTH_SHORT).show()
        }

    }

    override fun getListMatchNews(data : MutableLiveData<MatchHomeBaoMoiData>){
        if (Helpers.internet){ //online mode
            NewsRemote.loadListMatchHome(data)
        }

        else{ //offline mode
//            NewsLocal.loadListMatchHome(data)
//            Toast.makeText(context,"No Internet",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getListCompetitionNews(data: MutableLiveData<CompetitionHomeBaoMoiData>) {
        if (Helpers.internet){ //online mode
            NewsRemote.loadCompetitionHome(data)
        }

        else{ //offline mode
//            NewsLocal.loadListMatchHome(data)
//            Toast.makeText(context,"No Internet",Toast.LENGTH_SHORT).show()
        }
    }

    override suspend fun getDetailNews(data : MutableLiveData<DetailBaoMoiData>,id: Int,context: Context?) {
        if (Helpers.internet){
            //online mode

            //load data from local
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

}