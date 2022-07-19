package com.example.football.view.broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import com.example.football.utils.Helpers
import com.example.football.utils.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CheckConnectionReceiver(listenerMain: CheckConnectionListener) : BroadcastReceiver() {
    //using to know broadcast is call how many time
    private var timeCall: Int = 0

    private var listener : CheckConnectionListener = listenerMain

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
            try{
                listener.transMode(internet = Network.isNetworkAvailable(context), timeCall)
                timeCall++
            }
            catch (e: NullPointerException){
                e.printStackTrace()
            }

    }
}