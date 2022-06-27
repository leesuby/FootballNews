package com.example.football.view

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.football.R
import com.example.football.utils.Helpers
import com.example.football.utils.ManagePermissions
import com.example.football.view.broadcast.CheckConnectionReceiver
import com.example.football.view.service.OfflineService
import com.example.football.viewmodel.NewsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() , HomeNewsFragment.GetIDContent {
    private val viewModel: NewsViewModel by viewModels()
    private val managePermissions : ManagePermissions = ManagePermissions(this)
    private val checkConnectionReceiver : CheckConnectionReceiver = CheckConnectionReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //register broadcast to check internet
        registerNetworkReceiver()

        //check permission for save data on local for OFFLINE mode
        if(!managePermissions.checkPermission()){
            managePermissions.showAlert()
        }
        else{
            Helpers.isOfflineMode = true

            //start service
            if(Helpers.internet){
                val intent = Intent(this, OfflineService::class.java)
                startService(intent)
            }
            //start fragment
            val fragment = HomeNewsFragment()
            fragment.getIDContent = this
            showFragment(fragment, false)
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(checkConnectionReceiver)

    }

    private fun registerNetworkReceiver(){
        Helpers.internet=CheckConnectionReceiver.isNetworkAvailable(this)

        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(checkConnectionReceiver, intentFilter)

    }

    fun showFragment (fragment: Fragment,addtoBackStack : Boolean = true){
        val fram = supportFragmentManager.beginTransaction()
        fram.replace(R.id.fragment_main,fragment)

        if (!addtoBackStack)
            fram.commit()
        else
            fram.addToBackStack(fragment.toString()).commit()

    }

    override fun showDetail(idContent: Int) {
        val fragment = DetailNewFragment()

        val bundle = Bundle()
        bundle.putInt("idContent", idContent)
        fragment.arguments = bundle

        showFragment(fragment)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            //Title bar back press triggers onBackPressed()
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    //Both navigation bar back press and title bar back press will trigger this method
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }


    //Check request for Android 9 below
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            123 -> if (grantResults.isNotEmpty()) {
                val READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                    // perform action when allow permission success
                    Helpers.isOfflineMode=true

                    val fragment = HomeNewsFragment()
                    fragment.getIDContent = this
                    showFragment(fragment, false)

                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    // Check request for android 11 above
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    Helpers.isOfflineMode=true

                    val fragment = HomeNewsFragment()
                    fragment.getIDContent = this
                    showFragment(fragment, false)

                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


}