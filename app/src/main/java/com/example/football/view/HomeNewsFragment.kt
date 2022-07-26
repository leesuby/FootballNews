package com.example.football.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.football.view.adapters.HomeAdapter
import com.example.football.R
import com.example.football.data.model.Content
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.utils.Global
import com.example.football.view.decor.HomeDecorate
import com.example.football.view.service.OfflineService
import com.example.football.viewmodel.NewsViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HomeNewsFragment : Fragment(), CoroutineScope, SwipeRefreshLayout.OnRefreshListener {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var adapterNewlist: HomeAdapter
    private lateinit var newsList: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout
    private val newsViewModel: NewsViewModel by activityViewModels()
    private lateinit var views: View

    private lateinit var mService: OfflineService

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    interface GetIDContent {
        fun showDetail(idContent: Int)
    }

    lateinit var getIDContent: GetIDContent

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //check Activity is created to get service
        activity?.lifecycle?.addObserver(ActivityLifeCycleObserver {
            if (Global.internet) {
                val mainActivity = activity as MainActivity
                mService = mainActivity.mService
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapterNewlist = HomeAdapter()

        //get data for home
        CoroutineScope(coroutineContext).launch {
            launch { newsViewModel.getListNews(context, loadOnline = false) }
            launch { newsViewModel.getListMatch() }
            launch { newsViewModel.getListCompetition() }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        views = inflater.inflate(R.layout.fragment_home_news, container, false)
        return views
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //init view
        super.onViewCreated(view, savedInstanceState)
        initView()
        bindViewModel()
    }


    override fun onResume() {
        super.onResume()
        //check fragment is refreshing
        if(swipeLayout.isRefreshing){
            swipeLayout.isRefreshing=false
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun initView() {

        //swipe layout
        swipeLayout = views.findViewById(R.id.swipe_homeLayout)
        swipeLayout.setOnRefreshListener(this)


        //recycler view
        layoutManager = LinearLayoutManager(this.context)
        newsList = views.findViewById(R.id.RV_news)
        newsList.layoutManager = layoutManager

        //adapter for recycler view
        adapterNewlist = HomeAdapter()

        adapterNewlist.setOnItemClickListener(object : HomeAdapter.onNewsClickListener {
            override fun onItemClick(idContent: Int) {
                getIDContent.showDetail(idContent)
            }
        })
        newsList.adapter = adapterNewlist

        //event listener for recycler view
        newsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (Global.internet) {
                        if (!adapterNewlist.checkLoading() && !swipeLayout.isRefreshing && Global.serviceIsBound) {
                            adapterNewlist.setLoading(true)
                            newsViewModel.getListNews(
                                this@HomeNewsFragment.context,
                                newsViewModel.getPage(),
                                loadOnline = true
                            )
                        }
                    } else Toast.makeText(activity, "Không có kết nối mạng", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    fun bindViewModel(){
        //News ViewModel
        newsViewModel.getListNewsObservable().observe(viewLifecycleOwner) {


            if (it == null) {

            } else {
                if (adapterNewlist.checkLoading()) {
                    var tmpList = mutableListOf<Content>()
                    tmpList.addAll(adapterNewlist.listNews)
                    tmpList.addAll(it.data.contents.toMutableList())

                    //load data to UI
                    adapterNewlist.setNewsList(tmpList)

                    Log.e("content_load","reach here")

                    newsViewModel.increasePage()
                    adapterNewlist.setLoading(false)

                    //save data for offline
                    val tmp = MutableLiveData<HomeBaoMoiData>()
                    tmp.value = it
                    mService.saveData(tmp)

                } else {
                    if (Global.contentSave.isNotEmpty()){
                        Log.e("content_Save","reach here")
                        adapterNewlist.setNewsList(Global.contentSave)
                    }
                    else
                    {
                        if(swipeLayout.isRefreshing){
                            swipeLayout.isRefreshing=false
                        }
                        adapterNewlist.listNews=it.data.contents.toMutableList()
                        adapterNewlist.notifyDataSetChanged()
                    }
                }


            }
        }

        //Match ViewModel
        newsViewModel.getListMatchObservable().observe(viewLifecycleOwner) {
            if (it == null) {

            } else {
                adapterNewlist.listMatch = it.data.soccerMatch.toMutableList()
                adapterNewlist.notifyDataSetChanged()
            }
        }

        //Competition ViewModel
        newsViewModel.getListCompetitionObservable().observe(viewLifecycleOwner) {
            if (it == null) {

            } else {
                adapterNewlist.listCompetition = it.data.soccerCompetitions.toMutableList()
                adapterNewlist.notifyDataSetChanged()
            }
        }

    }


    override fun onPause() {
        super.onPause()

        //save data which have been loading
        adapterNewlist.listNews.forEach {
            if (!Global.contentSave.contains(it))
                Global.contentSave.add(it)
        }
    }



    //class to check if activity created
    inner class ActivityLifeCycleObserver(private val update: () -> Unit) :
        DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            owner.lifecycle.removeObserver(this)
            update()
        }
    }

    //on refresh list
    override fun onRefresh() {

        CoroutineScope(coroutineContext).launch {
            //reset page to 0
            newsViewModel.resetPage()

            //reset data
            Global.contentSave = mutableListOf()

            //get new data
            launch { newsViewModel.getListNews(
                context,
                0,
                loadOnline = true) }

            launch{
                newsViewModel.getListMatch()
            }

            launch {
                newsViewModel.getListCompetition()
            }



            //increase page
            newsViewModel.increasePage()

        }
    }
}



