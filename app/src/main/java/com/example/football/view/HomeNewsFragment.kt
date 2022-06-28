package com.example.football.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.football.view.adapters.RecyclerHomeAdapter
import com.example.football.data.model.HomeBaoMoiData
import com.example.football.R
import com.example.football.data.model.SoccerMatch
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

    interface GetIDContent{

        fun showDetail(idContent: Int)
    }

    lateinit var  getIDContent: GetIDContent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapterNewlist = RecyclerHomeAdapter()
        CoroutineScope(coroutineContext).launch {
            newsViewModel.getListNews(context)
            newsViewModel.getListMatch()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_news,container,false)
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

        newsViewModel.getListNewsObservable().observe(viewLifecycleOwner) {
            if (it == null) {
                Toast.makeText(this.context, "No result found", Toast.LENGTH_SHORT).show()
            } else {
                adapterNewlist.listNews = it.data.contents.toMutableList()
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


    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}

