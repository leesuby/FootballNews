package com.example.football.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.football.view.adapters.RecyclerHomeAdapter
import com.example.football.R
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.utils.Helpers
import com.example.football.view.decor.HomeDecorate
import com.example.football.view.service.OfflineService
import com.example.football.viewmodel.NewsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HomeNewsFragment : Fragment(), CoroutineScope {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var adapterNewlist: RecyclerHomeAdapter
    private lateinit var newsList: RecyclerView
    private val newsViewModel: NewsViewModel by activityViewModels()
    private lateinit var views: View

    private lateinit var mService: OfflineService


    interface GetIDContent {

        fun showDetail(idContent: Int)
    }

    lateinit var getIDContent: GetIDContent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //check Activity is created to get service
        activity?.lifecycle?.addObserver(ActivityLifeCycleObserver {
            if (Helpers.internet) {
                val mainActivity = activity as MainActivity
                mService = mainActivity.mService
            }
        })
    }

    override fun onDetach() {

        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        adapterNewlist = RecyclerHomeAdapter()
        //get data for home
        CoroutineScope(coroutineContext).launch {
            newsViewModel.getListNews(context, loadOnline = false)
            newsViewModel.getListMatch()
            newsViewModel.getListCompetition()
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
    }

    override fun onDestroy() {
        //set
        super.onDestroy()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun initView() {


        layoutManager = LinearLayoutManager(this.context)
        newsList = views.findViewById(R.id.RV_news)
        newsList.layoutManager = layoutManager

        adapterNewlist = RecyclerHomeAdapter()

        adapterNewlist.setOnItemClickListener(object : RecyclerHomeAdapter.onNewsClickListener {
            override fun onItemClick(idContent: Int) {
                getIDContent.showDetail(idContent)
            }
        })
        newsList.adapter = adapterNewlist
        newsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (Helpers.internet) {
                        if (!adapterNewlist.checkLoading()) {
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

        this.context?.let { HomeDecorate(it, R.drawable.line_divider) }
            ?.let { newsList.addItemDecoration(it) }

        newsViewModel.getListNewsObservable().observe(viewLifecycleOwner) {
            if (it == null) {
                Toast.makeText(this.context, "No result found", Toast.LENGTH_SHORT).show()
            } else {
                if (adapterNewlist.checkLoading()) {
                    //load data to UI
                    adapterNewlist.listNews.addAll(it.data.contents.toMutableList())
                    newsViewModel.increasePage()
                    adapterNewlist.setLoading(false)

                    //save data for offline
                    val tmp = MutableLiveData<HomeBaoMoiData>()
                    tmp.value = it
                    mService.saveData(tmp)

                } else {
                    if (Helpers.contentSave.isNotEmpty())
                        adapterNewlist.listNews = Helpers.contentSave
                    else
                        adapterNewlist.listNews = it.data.contents.toMutableList()
                }

                adapterNewlist.notifyDataSetChanged()
            }
        }

        newsViewModel.getListMatchObservable().observe(viewLifecycleOwner) {
            if (it == null) {
                Toast.makeText(this.context, "No result found", Toast.LENGTH_SHORT).show()
            } else {
                adapterNewlist.listMatch = it.data.soccer_match.toMutableList()
                adapterNewlist.notifyDataSetChanged()
            }
        }

        newsViewModel.getListCompetitionObservable().observe(viewLifecycleOwner) {
            if (it == null) {
                Toast.makeText(this.context, "No result found", Toast.LENGTH_SHORT).show()
            } else {
                adapterNewlist.listCompetition = it.data.soccer_competitions.toMutableList()
                adapterNewlist.notifyDataSetChanged()
            }
        }

    }

    //save data which have been loading
    override fun onPause() {
        super.onPause()

        adapterNewlist.listNews.forEach {
            if (!Helpers.contentSave.contains(it))
                Helpers.contentSave.add(it)
        }
    }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO


    //class to check if activity created
    inner class ActivityLifeCycleObserver(private val update: () -> Unit) :
        DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            owner.lifecycle.removeObserver(this)
            update()
        }
    }

}

