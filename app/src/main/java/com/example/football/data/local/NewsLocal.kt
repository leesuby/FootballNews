package com.example.football.data.local

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.football.data.local.database.BaoMoiDatabase
import com.example.football.data.local.database.HomeContent
import com.example.football.data.local.database.HomeContentDao
import com.example.football.data.model.Data
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.utils.Helpers
import com.example.football.utils.MainApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewsLocal {
    companion object{
        var homeContentDao : HomeContentDao = BaoMoiDatabase.getDatabase().homeContentDao()

        suspend fun saveData(homeData : MutableLiveData<HomeBaoMoiData>){
            saveListNews(homeData)

        }

        //save list news to local
        suspend fun saveListNews(homeData: MutableLiveData<HomeBaoMoiData>){
            for (content in homeData.value!!.data!!.contents)
            {
                var homeContent = HomeContent(
                    content.content_id,
                    content.title,
                    content.date,
                    Helpers.mLoad(content.publisher_logo),
                    Helpers.mLoad(content.avatar_url))

                homeContentDao.addContent(homeContent)
            }
        }

        //load list news from local
        fun loadListNews( data: MutableLiveData<HomeBaoMoiData>){

            GlobalScope.launch(Dispatchers.IO) {
                data.postValue(HomeBaoMoiData(Data(null,Helpers.convert(homeContentDao.readAllContentSynchronous())),0,""))
            }

        }

    }
}