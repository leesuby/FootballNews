package com.example.football.view


import android.content.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.football.R
import com.example.football.utils.Helpers
import com.example.football.utils.ManagePermissions
import com.example.football.view.broadcast.CheckConnectionListener
import com.example.football.view.broadcast.CheckConnectionReceiver
import com.example.football.view.service.OfflineService
import com.example.football.viewmodel.NewsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() , HomeNewsFragment.GetIDContent , CheckConnectionListener{
    private val viewModel: NewsViewModel by viewModels()
    private val managePermissions : ManagePermissions = ManagePermissions(this)
    private val checkConnectionReceiver : CheckConnectionReceiver = CheckConnectionReceiver(this)
    private lateinit var actionAppBar : Toolbar
    private lateinit var navBar : BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navControl : NavController

    lateinit var mService: OfflineService
    private var mBound: Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to OfflineService, cast the IBinder and get OfflineService instance
            val binder = service as OfflineService.OfflineBinder
            mService = binder.getService()
            mBound  = true
            Helpers.serviceIsBound= true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
            Helpers.serviceIsBound= false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get cache path to save local data
        Helpers.cacheDir = this.cacheDir.toString()

        //initial view
        initView()

        //splash screen for waiting download data
        startSplashScreen()

        //register broadcast to check internet
        registerNetworkReceiver()

        //check permission for save data on local for OFFLINE mode
        if(!managePermissions.checkPermission()){
            managePermissions.showAlert()
        }
        else{
            loadHomePage(true)
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(checkConnectionReceiver)
        if(mBound)
        unbindService(connection)

        Helpers.contentSave= mutableListOf()

    }

    private fun initView(){
        drawerLayout = findViewById(R.id.layout_drawer)
        navBar = findViewById(R.id.bottomNavigationView)

        navBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> showFragment(HomeNewsFragment(),false)
                R.id.nav_schedule -> showFragment(ScheduleNewsFragment(),false)
                R.id.nav_club -> showFragment(ClubNewsFragment(),false)
                R.id.nav_standing -> showFragment(StandingNewsFragment(),false)
            }
            true
        }

        actionAppBar = findViewById(R.id.toolbar)

        //set up for tool bar
        setupToolbar()

        //set up for toggle
        setupDrawerToggle()

        //set up for bottom navigator bar
        setupNavBar()

    }
    private fun setupToolbar(){
        actionAppBar.title=""

        setSupportActionBar(actionAppBar)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun setupDrawerToggle() {
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            actionAppBar,
            R.string.app_name,
            R.string.app_name
        )

        //This is necessary to change the icon of the Drawer Toggle upon state change.
        toggle.toolbarNavigationClickListener = View.OnClickListener {
            if (toggle.isDrawerIndicatorEnabled) {
                drawerLayout.openDrawer(GravityCompat.START)
            } else {
                onBackPressed()
            }
        }

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupNavBar(){
        navBar.setOnItemSelectedListener {
            Log.e("itemid",it.itemId.toString())
            when(it.itemId){
                R.id.nav_home -> {val fragmentHome = HomeNewsFragment()
                                    fragmentHome.getIDContent = this@MainActivity
                                    showFragment(fragmentHome, false)}
                R.id.nav_schedule -> showFragment(ScheduleNewsFragment(),false)
                R.id.nav_club -> showFragment(ClubNewsFragment(),false)
                R.id.nav_standing -> showFragment(StandingNewsFragment(),false)
            }
            true
        }
    }

    private fun setupNavControl(){
        navBar.setupWithNavController(navControl)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
        lp.matchConstraintPercentHeight = 0.95f
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
    fun loadHomePage(offlineMode : Boolean){
        Helpers.isOfflineMode = offlineMode

        if(Helpers.internet){



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
                    lp.matchConstraintPercentHeight = 0.81f
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
            val mConstrainLayout = findViewById<FrameLayout>(R.id.fragment_main)
            val lp = mConstrainLayout.layoutParams as ConstraintLayout.LayoutParams
            lp.matchConstraintPercentHeight = 0.81f
            mConstrainLayout.layoutParams = lp


            actionAppBar.visibility= View.VISIBLE
            navBar.visibility= View.VISIBLE

            //start fragment
            val fragmentHome = HomeNewsFragment()
            fragmentHome.getIDContent = this@MainActivity
            showFragment(fragmentHome, false)

        }

    }

    private fun showFragment (fragment: Fragment, addtoBackStack : Boolean = true): Boolean {
        val fram = supportFragmentManager.beginTransaction()
        fram.replace(R.id.fragment_main,fragment)

        if (!addtoBackStack)
            fram.commit()
        else
            fram.addToBackStack(fragment.toString()).commit()

        return true
    }

    override fun showDetail(idContent: Int) {
        val fragment = DetailNewFragment()

        val bundle = Bundle()
        bundle.putInt("idContent", idContent)
        fragment.arguments = bundle

        showFragment(fragment)

        val mConstrainLayout = findViewById<FrameLayout>(R.id.fragment_main)
        val lp = mConstrainLayout.layoutParams as ConstraintLayout.LayoutParams
        lp.matchConstraintPercentHeight = 0.90f
        mConstrainLayout.layoutParams = lp

        navBar.visibility= View.GONE

        showBackButton(true)

    }

    private fun showBackButton(isBack: Boolean) {
        toggle.isDrawerIndicatorEnabled = !isBack
        supportActionBar!!.setDisplayHomeAsUpEnabled(isBack)

        toggle.syncState()
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
            if(supportFragmentManager.backStackEntryCount==1)
            {
                showBackButton(false)

                val mConstrainLayout = findViewById<FrameLayout>(R.id.fragment_main)
                val lp = mConstrainLayout.layoutParams as ConstraintLayout.LayoutParams
                lp.matchConstraintPercentHeight = 0.81f
                mConstrainLayout.layoutParams = lp

                navBar.visibility= View.VISIBLE

            }
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
                    loadHomePage(true)
                    }
                } else {
                    loadHomePage(false)
                }
            }
        }


    // Check request for android 11 above
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    loadHomePage(true)
                    }
                } else {
                    loadHomePage(false)
                }
            }
        }

    override fun transMode(internet: Boolean,timeCall: Int) {
        Helpers.internet=internet
        Log.e("timecall1",timeCall.toString())

        if(timeCall==0 && internet){
            //bind service
            Intent(this, OfflineService::class.java).also { intent ->
                bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
        }

        if(timeCall!=0 && !mBound && Helpers.internet){
            Log.e("timecall",timeCall.toString())
            finish();
            startActivity(intent);

            // this basically provides animation
            overridePendingTransition(0, 0);
        }

    }
}

