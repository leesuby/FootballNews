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

object NewsLocal {
    //DAO
    var homeContentDao: HomeContentDao = BaoMoiDatabase.getDatabase().HomeContentDao()
    var detailContentDao: DetailContentDao = BaoMoiDatabase.getDatabase().DetailContentDao()
    var relatedContentDao: RelatedContentDao = BaoMoiDatabase.getDatabase().RelatedContentDao()
    var bodyDetailContentDao: BodyDetailContentDao =
        BaoMoiDatabase.getDatabase().BodyDetailContentDao()

    suspend fun saveData(homeData: MutableLiveData<HomeBaoMoiData>) {
        saveListNews(homeData)
        saveDetailNews(homeData)
    }

    //save list news to local
    private suspend fun saveListNews(homeData: MutableLiveData<HomeBaoMoiData>) {
        for (content in homeData.value!!.data!!.contents) {
            var homeContent = HomeContent(
                content.contentId,
                content.title,
                content.date,
                Helpers.saveImageToExternalStorage(
                    content.publisherLogo,
                    content.contentId.toString(),
                    "logo"
                ),
                Helpers.saveImageToExternalStorage(
                    content.avatarUrl,
                    content.contentId.toString(),
                    "avatar"
                ),
                publisherLogoURL = content.publisherLogo,
                avatarURL = content.avatarUrl
            )

            homeContentDao.addContent(homeContent)
        }

        //list news is saved to database
        Helpers.isListNewsSaved = true
    }

    //save detail contents news to local
    private fun saveDetailNews(homeData: MutableLiveData<HomeBaoMoiData>) {
        for (content in homeData.value!!.data!!.contents) {
            var getDataFromRetro: MutableLiveData<DetailBaoMoiData> = MutableLiveData()
            NewsRemote.loadContentNews(getDataFromRetro, content.contentId, true)
        }
    }

    //get detail content from retrofit
    suspend fun saveDetailContentNews(getDataFromRetro: MutableLiveData<DetailBaoMoiData>) {

        //TODO: Solve Null case
        if (getDataFromRetro.value == null || getDataFromRetro.value!!.data == null)
            return

        var content = getDataFromRetro.value!!.data.content
        var related = getDataFromRetro.value!!.data.related.contents


        //get body of news
        var num = 0
        for (body in content.body) {
            var bodyContent = BodyDetailContent(
                contentId = content.contentId,
                type = body.type,
                content = body.content,
                originUrl = Helpers.saveImageToExternalStorage(
                    body.originUrl,
                    content.contentId.toString(),
                    num.toString()
                ),
                subtype = body.subtype,
                bodyId = num
            )
            bodyDetailContentDao.addBodyDetailContent(bodyContent)
            num++
        }

        //get related news
        for (relatedNews in related) {
            var relatedContent = RelatedContent(
                contentId = relatedNews.contentId,
                relatedId = content.contentId,
                title = relatedNews.title,
                date = relatedNews.date,
                avatar = Helpers.saveImageToExternalStorage(
                    relatedNews.avatarUrl,
                    relatedNews.contentId.toString(),
                    "avatar"
                ),
                publisherLogo = Helpers.saveImageToExternalStorage(
                    relatedNews.publisherLogo,
                    relatedNews.contentId.toString(),
                    "logo"
                ),
                publisherLogoURL = content.publisherLogo,
                avatarURL = content.avatarUrl
            )

            relatedContentDao.addRelatedContent(relatedContent)
        }

        //get basic info of news
        var detailContent = DetailContent(
            content.contentId,
            content.title,
            content.date,
            content.description
        )
        detailContentDao.addDetailContent(detailContent)
    }

    //load list news from local
    fun loadListNewsByPage(data: MutableLiveData<HomeBaoMoiData>, page: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            data.postValue(
                HomeBaoMoiData(
                    Data(
                        null,
                        Helpers.convert(homeContentDao.readAllContentSynchronousByPage(page))
                    ), 0, ""
                )
            )
        }

    }

    fun loadListNewsAll(data: MutableLiveData<HomeBaoMoiData>) {
        GlobalScope.launch(Dispatchers.IO) {
            data.postValue(
                HomeBaoMoiData(
                    Data(
                        null,
                        Helpers.convert(homeContentDao.readAllContentSynchronous())
                    ), 0, ""
                )
            )
        }

    }

    fun loadListSearch(data: MutableLiveData<HomeBaoMoiData>, keyword: String) {
        GlobalScope.launch(Dispatchers.IO) {
            data.postValue(
                HomeBaoMoiData(
                    Data(
                        null,
                        Helpers.convert(homeContentDao.searchByKeyWord(keyword))
                    ), 0, ""
                )
            )
        }
    }

    fun loadDetailContent(data: MutableLiveData<DetailBaoMoiData>, id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            if (detailContentDao.readAllSynchronous(id).isEmpty()) {
                data.postValue(null)
            } else {
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