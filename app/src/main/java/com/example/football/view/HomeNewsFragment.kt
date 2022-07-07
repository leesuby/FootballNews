package com.example.football.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.football.view.adapters.RecyclerHomeAdapter
import com.example.football.R
import com.example.football.utils.Helpers
import com.example.football.view.compose.LoadingAnimation
import com.example.football.view.decor.HomeDecorate
import com.example.football.viewmodel.NewsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HomeNewsFragment : Fragment() , CoroutineScope {
    private var layoutManager : RecyclerView.LayoutManager? = null
    private lateinit var adapterNewlist : RecyclerHomeAdapter
    private lateinit var newsList : RecyclerView
    private val newsViewModel : NewsViewModel by activityViewModels()
    private lateinit var views :View
    private var flagLoading = false
    private var pageLoad : Int = 0

    interface GetIDContent{

        fun showDetail(idContent: Int)
    }

    lateinit var  getIDContent: GetIDContent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //for recreate fragment
        pageLoad=1

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
        views = inflater.inflate(R.layout.fragment_home_news,container,false)
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
    fun initView(){
        layoutManager = LinearLayoutManager(this.context)
        newsList = views.findViewById(R.id.RV_news)
        newsList.layoutManager = layoutManager



        adapterNewlist = RecyclerHomeAdapter()

        adapterNewlist.setOnItemClickListener(object : RecyclerHomeAdapter.onNewsClickListener{
            override fun onItemClick(idContent: Int) {
                getIDContent.showDetail(idContent)
            }
        })
        newsList.adapter = adapterNewlist
        newsList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    if(!flagLoading){
                        flagLoading=true
                        newsViewModel.getListNews(this@HomeNewsFragment.context,pageLoad,loadOnline = true)

                    }

                }
            }
        })

        this.context?.let { HomeDecorate(it,R.drawable.line_divider) }
            ?.let { newsList.addItemDecoration(it) }

        newsViewModel.getListNewsObservable().observe(viewLifecycleOwner) {
            if (it == null) {
                Toast.makeText(this.context, "No result found", Toast.LENGTH_SHORT).show()
            } else {
                if(flagLoading){
                    adapterNewlist.listNews.addAll(it.data.contents.toMutableList())
                    pageLoad++
                    flagLoading=false
                }
                else {
                    if(Helpers.contentSave.isNotEmpty())
                        adapterNewlist.listNews=Helpers.contentSave
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

        newsViewModel.getListCompetitionObservable().observe(viewLifecycleOwner){
            if (it == null) {
                Toast.makeText(this.context, "No result found", Toast.LENGTH_SHORT).show()
            } else {
                adapterNewlist.listCompetition = it.data.soccer_competitions.toMutableList()
                adapterNewlist.notifyDataSetChanged()
            }
        }

    }

    //save data which have been loading
    override fun onStop() {
        super.onStop()

        adapterNewlist.listNews.forEach {
            if(!Helpers.contentSave.contains(it))
                Helpers.contentSave.add(it)
        }
    }



    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}

