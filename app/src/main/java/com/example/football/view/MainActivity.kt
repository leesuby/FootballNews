package com.example.football.view

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.football.R
import com.example.football.utils.Helpers
import com.example.football.utils.ManagePermissions
import com.example.football.viewmodel.NewsViewModel


class MainActivity : AppCompatActivity() , HomeNewsFragment.GetIDContent {
    private val viewModel: NewsViewModel by viewModels()
    private val managePermissions : ManagePermissions = ManagePermissions(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //check permission for save data on local for OFFLINE mode
        if(!managePermissions.checkPermission()){
            managePermissions.showAlert()
        }
        else{
            Helpers.isOfflineMode = true
            val fragment = HomeNewsFragment()
            fragment.getIDContent = this
            showFragment(fragment, false)
        }

    }


    fun showFragment (fragment: Fragment,addtoBackStack : Boolean = true){
        val fram = supportFragmentManager.beginTransaction()
        fram.replace(R.id.fragment_main,fragment)

        if (addtoBackStack == false)
            fram.commit()
        else
            fram.addToBackStack("${fragment.toString()}").commit()

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
            123 -> if (grantResults.size > 0) {
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