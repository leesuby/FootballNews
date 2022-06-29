package com.example.football.view


import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.football.R
import com.example.football.utils.Helpers
import com.example.football.utils.ManagePermissions
import com.example.football.view.broadcast.CheckConnectionReceiver
import com.example.football.view.service.OfflineService
import com.example.football.viewmodel.NewsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() , HomeNewsFragment.GetIDContent {
    private val viewModel: NewsViewModel by viewModels()
    private val managePermissions : ManagePermissions = ManagePermissions(this)
    private val checkConnectionReceiver : CheckConnectionReceiver = CheckConnectionReceiver()
    private lateinit var actionAppBar : Toolbar
    private lateinit var navBar : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionAppBar = findViewById(R.id.toolbar)
        navBar = findViewById(R.id.bottomNavigationView)

//        navBar.setupWithNavController(findNavController(R.id.fragment_main))

        //splash screen for waiting download data
        startSplashScreen()

        //register broadcast to check internet
        registerNetworkReceiver()

        //check permission for save data on local for OFFLINE mode
        if(!managePermissions.checkPermission()){
            managePermissions.showAlert()
        }
        else{
            Helpers.isOfflineMode = true

            loadListonOfflineMode()

        }


    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(checkConnectionReceiver)

    }

    private fun startSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)

        val mConstrainLayout = findViewById<FrameLayout>(R.id.fragment_main)
        val lp = mConstrainLayout.layoutParams as ConstraintLayout.LayoutParams
        lp.matchConstraintPercentHeight = 1f
        mConstrainLayout.layoutParams = lp

        val fragmentSplash = SplashFragment()
        showFragment(fragmentSplash, false)
    }

    private fun registerNetworkReceiver(){
        Helpers.internet=CheckConnectionReceiver.isNetworkAvailable(this)

        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(checkConnectionReceiver, intentFilter)

    }

    //use to load list if user enable write and read SD
    private fun loadListonOfflineMode(){
        Helpers.isOfflineMode = true

        //start service
        if(Helpers.internet){
            val intent = Intent(this, OfflineService::class.java)
            startService(intent)

            //Thread check data is saved and load list news
            GlobalScope.launch(Dispatchers.Default){
                while(!Helpers.isListNewsSaved){
                    if(Helpers.isListNewsSaved)
                        break

                }

                //Run UI on Main Thread(UI Thread)
                launch(Dispatchers.Main) {
                    val mConstrainLayout = findViewById<FrameLayout>(R.id.fragment_main)
                    val lp = mConstrainLayout.layoutParams as ConstraintLayout.LayoutParams
                    lp.matchConstraintPercentHeight = 0.84f
                    mConstrainLayout.layoutParams = lp

                    actionAppBar.visibility= View.VISIBLE
                    navBar.visibility= View.VISIBLE

                }


                //start fragment
                val fragmentHome = HomeNewsFragment()
                fragmentHome.getIDContent = this@MainActivity
                showFragment(fragmentHome, false)
            }
        }

        else{
            //start fragment
            val fragmentHome = HomeNewsFragment()
            fragmentHome.getIDContent = this@MainActivity
            showFragment(fragmentHome, false)

        }

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
        if (item.getItemId() == android.R.id.home) {
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
                    loadListonOfflineMode()
                    }
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


    // Check request for android 11 above
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    loadListonOfflineMode()
                    }
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

