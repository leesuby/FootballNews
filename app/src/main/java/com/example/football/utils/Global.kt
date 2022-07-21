package com.example.football.utils

import com.example.football.data.model.Content

object Global {
    //check internet of User(Broadcast will update)
    var internet: Boolean = true

    //check user enable offline mode by provide acceptance to write and read from sdcard
    var isOfflineMode: Boolean = false

    //check list news is save for Splash screen
    var isListNewsSaved: Boolean = false

    //Just app name
    val AppName = "BongDaMoi"

    //Seperator for save file
    val seperator = "/"

    //check service is bound when app start
    var serviceIsBound = false

    //get cache dir on main activity
    var cacheDir: String = ""

    //save content news for Home if user onPause, onStop or something else which lost data
    var contentSave: MutableList<Content> = mutableListOf()

    //calculate time
    var curtime: Long = 0
}