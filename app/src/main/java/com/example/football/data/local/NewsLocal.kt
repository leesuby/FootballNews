package com.example.football.data.local

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.football.data.local.database.BaoMoiDatabase
import com.example.football.data.local.database.detail.*
import com.example.football.data.local.database.home.HomeContent
import com.example.football.data.local.database.home.HomeContentDao
import com.example.football.data.model.Data
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.data.model.detail.DetailBaoMoiData
import com.example.football.data.model.detail.Related
import com.example.football.data.remote.NewsRemote
import com.example.football.utils.Helpers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewsLocal {
    companion object{
        //DAO
        var homeContentDao : HomeContentDao = BaoMoiDatabase.getDatabase().HomeContentDao()
        var detailContentDao : DetailContentDao = BaoMoiDatabase.getDatabase().DetailContentDao()
        var relatedContentDao : RelatedContentDao = BaoMoiDatabase.getDatabase().RelatedContentDao()
        var bodyDetailContentDao : BodyDetailContentDao = BaoMoiDatabase.getDatabase().BodyDetailContentDao()

        suspend fun saveData(homeData : MutableLiveData<HomeBaoMoiData>){
            saveListNews(homeData)
            saveDetailNews(homeData)
        }

        //save list news to local
        private suspend fun saveListNews(homeData: MutableLiveData<HomeBaoMoiData>){
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

        //save detail contents news to local
        private fun saveDetailNews(homeData: MutableLiveData<HomeBaoMoiData>) {
            for (content in homeData.value!!.data!!.contents)
            {
                var getDataFromRetro : MutableLiveData<DetailBaoMoiData> = MutableLiveData()
                NewsRemote.loadContentNews(getDataFromRetro,content.content_id,true)
            }
        }

        //get detail content from retrofit
        suspend fun saveDetailContentNews(getDataFromRetro: MutableLiveData<DetailBaoMoiData>){

            //TODO: Solve Null case
            if (getDataFromRetro.value==null)
                return

            var content = getDataFromRetro.value!!.data.content
            var related = getDataFromRetro.value!!.data.related.contents

            //get basic info of news
            var detailContent = DetailContent(
                content.content_id,
                content.title,
                content.date,
                content.description
            )
            detailContentDao.addDetailContent(detailContent)

            //get body of news
            var num =0
            for(body in content.body){
                var bodyContent = BodyDetailContent(
                    content_id = content.content_id,
                    type = body.type,
                    content = body.content,
                    originUrl = Helpers.mLoad(body.originUrl),
                    subtype = body.subtype,
                    body_id = num
                )
                bodyDetailContentDao.addBodyDetailContent(bodyContent)
                num++
            }

            //get related news
            for (relatedNews in related){
                var relatedContent = RelatedContent(
                    content_id = relatedNews.content_id,
                    related_id = content.content_id,
                    title = relatedNews.title,
                    date = relatedNews.date,
                    avatar = Helpers.mLoad(relatedNews.avatar_url),
                    publisher_logo = Helpers.mLoad(relatedNews.publisher_logo)
                )

                relatedContentDao.addRelatedContent(relatedContent)
            }

        }

        //load list news from local
        fun loadListNews( data: MutableLiveData<HomeBaoMoiData>){
            GlobalScope.launch(Dispatchers.IO) {
                data.postValue(HomeBaoMoiData(Data(null,Helpers.convert(homeContentDao.readAllContentSynchronous())),0,""))
            }

        }

        fun loadDetailContent(data: MutableLiveData<DetailBaoMoiData>,id : Int){
            GlobalScope.launch(Dispatchers.IO) {
                data.postValue(
                    DetailBaoMoiData(
                        com.example.football.data.model.detail.Data(
                            "", Helpers.convert(
                                detailContentDao.readAllSynchronous(id),
                                bodyDetailContentDao.readAllSynchronous(id)
                            ),Related(Helpers.convert(relatedContentDao.readAllSynchronous(id)))
                        ),
                        0,
                        ""
                    )
                )
            }
        }

    }
}