package com.example.football.view.broadcast

//interface to communicate between Activity vs Broadcast
interface CheckConnectionListener {
    fun transMode(internet: Boolean,timeCall : Int)
}