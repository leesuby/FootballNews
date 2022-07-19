package com.example.football.data.local

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
import com.example.football.utils.Converter
import com.example.football.utils.Global
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//Object to save and load data from local
object NewsLocal {

    //DAO for home list news
    var homeContentDao: HomeContentDao = BaoMoiDatabase.getDatabase().HomeContentDao()

    //DAO for detail news (content of news)
    var detailContentDao: DetailContentDao = BaoMoiDatabase.getDatabase().DetailContentDao()

    //DAO for related news on detail news
    var relatedContentDao: RelatedContentDao = BaoMoiDatabase.getDatabase().RelatedContentDao()

    //DAO for body news on detail news
    var bodyDetailContentDao: BodyDetailContentDao =
        BaoMoiDatabase.getDatabase().BodyDetailContentDao()


    //Function to save news data from remote to local (Save news and content of it)
    fun saveData(homeData: MutableLiveData<HomeBaoMoiData>) {

        //Save list news
        saveListNews(homeData)

        //Save content of list news
        saveDetailNews(homeData)
    }

    //Save list news to local
    @OptIn(DelicateCoroutinesApi::class)
    private fun saveListNews(homeData: MutableLiveData<HomeBaoMoiData>) {
        for (content in homeData.value!!.data.contents) {
            val homeContent = HomeContent(
                content.contentId,
                content.title,
                content.date,
                Converter.saveImageToExternalStorage(
                    content.publisherLogo,
                    content.contentId.toString(),
                    "logo"
                ),
                Converter.saveImageToExternalStorage(
                    content.avatarUrl,
                    content.contentId.toString(),
                    "avatar"
                ),
                publisherLogoURL = content.publisherLogo,
                avatarURL = content.avatarUrl
            )

            GlobalScope.launch(Dispatchers.IO) {
                homeContentDao.addContent(homeContent)
            }

        }

        //List news is saved to database
        Global.isListNewsSaved = true
    }

    //Save detail contents news to local
    private fun saveDetailNews(homeData: MutableLiveData<HomeBaoMoiData>) {
        for (content in homeData.value!!.data.contents) {
            val getDataFromRetro: MutableLiveData<DetailBaoMoiData> = MutableLiveData()
            NewsRemote.loadContentNews(getDataFromRetro, content.contentId, true)
        }
    }

    //Get detail content from retrofit
    suspend fun saveDetailContentNews(getDataFromRetro: MutableLiveData<DetailBaoMoiData>) {

        //TODO: Solve Null case
        if (getDataFromRetro.value == null)
            return

        val content = getDataFromRetro.value!!.data.content
        val related = getDataFromRetro.value!!.data.related.contents


        //Get body of news
        var num = 0
        for (body in content.body) {
            val bodyContent = BodyDetailContent(
                contentId = content.contentId,
                type = body.type,
                content = body.content,
                originUrl = Converter.saveImageToExternalStorage(
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

        //Get related news
        for (relatedNews in related) {
            val relatedContent = RelatedContent(
                contentId = relatedNews.contentId,
                relatedId = content.contentId,
                title = relatedNews.title,
                date = relatedNews.date,
                avatar = Converter.saveImageToExternalStorage(
                    relatedNews.avatarUrl,
                    relatedNews.contentId.toString(),
                    "avatar"
                ),
                publisherLogo = Converter.saveImageToExternalStorage(
                    relatedNews.publisherLogo,
                    relatedNews.contentId.toString(),
                    "logo"
                ),
                publisherLogoURL = content.publisherLogo,
                avatarURL = content.avatarUrl
            )

            relatedContentDao.addRelatedContent(relatedContent)
        }

        //Get basic info of news
        val detailContent = DetailContent(
            content.contentId,
            content.title,
            content.date,
            content.description
        )
        detailContentDao.addDetailContent(detailContent)
    }

    //Load list news from local by page
    @OptIn(DelicateCoroutinesApi::class)
    fun loadListNewsByPage(data: MutableLiveData<HomeBaoMoiData>, page: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            data.postValue(
                HomeBaoMoiData(
                    Data(
                        null,
                        Converter.convert(homeContentDao.readAllContentSynchronousByPage(page))
                    ), 0, ""
                )
            )
        }

    }


    //Load list news all from local
    @OptIn(DelicateCoroutinesApi::class)
    fun loadListNewsAll(data: MutableLiveData<HomeBaoMoiData>) {
        GlobalScope.launch(Dispatchers.IO) {
            data.postValue(
                HomeBaoMoiData(
                    Data(
                        null,
                        Converter.convert(homeContentDao.readAllContentSynchronous())
                    ), 0, ""
                )
            )
        }

    }

    //Load list news by keyword user input
    @OptIn(DelicateCoroutinesApi::class)
    fun loadListSearch(data: MutableLiveData<HomeBaoMoiData>, keyword: String) {
        GlobalScope.launch(Dispatchers.IO) {
            data.postValue(
                HomeBaoMoiData(
                    Data(
                        null,
                        Converter.convert(homeContentDao.searchByKeyWord(keyword))
                    ), 0, ""
                )
            )
        }
    }


    //Load detail content of news
    @OptIn(DelicateCoroutinesApi::class)
    fun loadDetailContent(data: MutableLiveData<DetailBaoMoiData>, id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            if (detailContentDao.readAllSynchronous(id).isEmpty()) {
                data.postValue(null)
            } else {
                data.postValue(
                    DetailBaoMoiData(
                        com.example.football.data.model.detail.Data(
                            "",
                            Converter.convert(
                                detailContentDao.readAllSynchronous(id),
                                bodyDetailContentDao.readAllSynchronous(id)
                            ),
                            Related(Converter.convert(relatedContentDao.readAllSynchronous(id)))
                        ),
                        0,
                        ""
                    )
                )
            }
        }
    }

}