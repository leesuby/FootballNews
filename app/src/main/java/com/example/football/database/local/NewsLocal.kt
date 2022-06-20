package com.example.football.database.local

import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.football.database.model.Boxe
import com.example.football.database.model.Content
import com.example.football.database.model.Data
import com.example.football.database.model.HomeBaoMoiData
import com.example.football.database.model.home.ContentDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewsLocal {
    companion object{

        //save list news to local
        suspend fun saveListNews(contentDao: ContentDao,data: MutableLiveData<HomeBaoMoiData>){
            for (content in data.value!!.data!!.contents)
                    contentDao.addContent(content)

        }

        //load list news from local
        fun loadListNews(contentDao: ContentDao,data: MutableLiveData<HomeBaoMoiData>){

            GlobalScope.launch(Dispatchers.IO) {
                data.postValue(HomeBaoMoiData(Data(null,contentDao.readAllContentSynchronous()),0,""))
            }

        }

    }
}