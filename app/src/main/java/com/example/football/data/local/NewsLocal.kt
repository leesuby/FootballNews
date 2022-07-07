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
                Log.e("error",content.toString())
                var homeContent = HomeContent(
                    content.content_id,
                    content.title,
                    content.date,
                    Helpers.saveImageToExternalStorage(content.publisher_logo,content.content_id.toString(),"logo"),
                    Helpers.saveImageToExternalStorage(content.avatar_url,content.content_id.toString(),"avatar"))

                homeContentDao.addContent(homeContent)
            }

            //list news is saved to database
            Helpers.isListNewsSaved = true
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



            //get body of news
            var num =0
            for(body in content.body){
                var bodyContent = BodyDetailContent(
                    content_id = content.content_id,
                    type = body.type,
                    content = body.content,
                    originUrl = Helpers.saveImageToExternalStorage(body.originUrl,content.content_id.toString(),num.toString()),
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
                    avatar = Helpers.saveImageToExternalStorage(relatedNews.avatar_url,relatedNews.content_id.toString(),"avatar"),
                    publisher_logo = Helpers.saveImageToExternalStorage(relatedNews.publisher_logo,relatedNews.content_id.toString(),"logo")
                )

                relatedContentDao.addRelatedContent(relatedContent)
            }

            //get basic info of news
            var detailContent = DetailContent(
                content.content_id,
                content.title,
                content.date,
                content.description
            )
            detailContentDao.addDetailContent(detailContent)
        }

        //load list news from local
        fun loadListNewsByPage( data: MutableLiveData<HomeBaoMoiData>, page: Int){
            GlobalScope.launch(Dispatchers.IO) {
                data.postValue(HomeBaoMoiData(Data(null,Helpers.convert(homeContentDao.readAllContentSynchronousByPage(page))),0,""))
            }

        }

        fun loadListNewsAll( data: MutableLiveData<HomeBaoMoiData>){
            GlobalScope.launch(Dispatchers.IO) {
                data.postValue(HomeBaoMoiData(Data(null,Helpers.convert(homeContentDao.readAllContentSynchronous())),0,""))
            }

        }

        fun loadDetailContent(data: MutableLiveData<DetailBaoMoiData>,id : Int){
            GlobalScope.launch(Dispatchers.IO) {
                if(detailContentDao.readAllSynchronous(id).isEmpty()){
                    data.postValue(null)
                }
                else {
                    data.postValue(
                        DetailBaoMoiData(
                            com.example.football.data.model.detail.Data(
                                "",
                                Helpers.convert(
                                    detailContentDao.readAllSynchronous(id),
                                    bodyDetailContentDao.readAllSynchronous(id)
                                ),
                                Related(Helpers.convert(relatedContentDao.readAllSynchronous(id)))
                            ),
                            0,
                            ""
                        )
                    )
                }
            }
        }

    }
}