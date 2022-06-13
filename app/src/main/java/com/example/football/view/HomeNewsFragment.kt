package com.example.football.view

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
import com.example.football.adapters.RecyclerNewsAdapter
import com.example.football.model.HomeBaoMoiData
import com.example.football.R
import com.example.football.viewmodel.NewsViewModel

class HomeNewsFragment : Fragment() {
    private var layoutManager : RecyclerView.LayoutManager? = null
    private lateinit var adapterNewlist : RecyclerNewsAdapter
    private lateinit var newsList : RecyclerView
    private val newsViewModel : NewsViewModel by activityViewModels()

    interface GetIDContent{
        fun showDetail(idContent: Int)
    }

    lateinit var  getIDContent: GetIDContent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_news,container,false)

        layoutManager = LinearLayoutManager(this.context)
        newsList = view.findViewById(R.id.RV_news)
        newsList.layoutManager = layoutManager

        adapterNewlist = RecyclerNewsAdapter()
        adapterNewlist.setOnItemClickListener(object : RecyclerNewsAdapter.onItemClickListener{
            override fun onItemClick(idContent: Int) {
                getIDContent.showDetail(idContent)
            }
        })
        newsList.adapter = adapterNewlist

        newsViewModel.getListNewsObservable().observe(viewLifecycleOwner, Observer<HomeBaoMoiData>{
            if (it == null){
                Toast.makeText(this.context,"No result found", Toast.LENGTH_SHORT).show()
            }else{
                adapterNewlist.ListNews = it.data.contents.toMutableList()
                adapterNewlist.notifyDataSetChanged()
            }
        })
        newsViewModel.getListNews()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //init view
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        //set
        super.onDestroy()
    }

    fun initView(){

    }
}

