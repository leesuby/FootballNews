package com.example.football.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.football.data.model.Content
import com.example.football.R
import com.example.football.data.model.SoccerCompetition
import com.example.football.data.model.SoccerMatch
import com.example.football.utils.Global
import com.example.football.utils.Helpers
import com.example.football.utils.View.margin
import com.example.football.view.compose.LoadingAnimation
import com.example.football.view.customview.NewsCustomView

//Adapter for home page
class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MATCH = 0
    private val NEWS = 1
    private val COMPETITION = 2
    private val LOADING = 3

    private lateinit var mListener: onNewsClickListener

    var listNews = mutableListOf<Content>()
    var listMatch = mutableListOf<SoccerMatch>()
    var listCompetition = mutableListOf<SoccerCompetition>()

    private var isLoading = false


    //set list news by using difUtil not notifydatasetChange
    fun setNewsList(newNewsList: MutableList<Content>) {
        val diffUtil = NewsHomeDiffUtil(listNews, newNewsList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        listNews = newNewsList
        diffResult.dispatchUpdatesTo(this)
    }

    //adapter is loading more data
    fun setLoading(boolean: Boolean) {
        isLoading = boolean
    }

    //check if data is loading
    fun checkLoading(): Boolean {
        return isLoading
    }

    interface onNewsClickListener {
        fun onItemClick(idContent: Int)
    }

    fun setOnItemClickListener(listener: onNewsClickListener) {
        mListener = listener
    }

    override fun getItemCount(): Int {
        return listNews.size + 3 //loading animate + match + competition
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> MATCH
            4 -> COMPETITION
            itemCount - 1 -> LOADING
            else -> NEWS
        }
    }

    //Holder for news
    inner class ViewHolderNews(itemView: View, listener: onNewsClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var customNews: NewsCustomView

        init {
            customNews = itemView.findViewById(R.id.custom_news)
            itemView.setOnClickListener {
                var positionListNews = 0
                when (adapterPosition) {
                    in 1..3 -> positionListNews = adapterPosition - 1
                    in 5..itemCount -> positionListNews = adapterPosition - 2
                }
                listener.onItemClick(listNews[positionListNews].contentId)
            }
        }

    }

    //Holder for match
    inner class ViewHolderMatch(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var itemMatchRecyclerView: RecyclerView
        var itemNoInternet: TextView

        init {
            itemMatchRecyclerView = itemView.findViewById(R.id.RV_homeMatch)
            itemNoInternet = itemView.findViewById(R.id.tv_nointernet_competition)
        }

    }


    //Holder for competition
    inner class ViewHolderCompetition(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var itemCompetitionRecyclerView: RecyclerView
        var itemNoInternet: TextView

        init {
            itemCompetitionRecyclerView = itemView.findViewById(R.id.RV_homeCompetition)
            itemNoInternet = itemView.findViewById(R.id.tv_nointernet_competition)
        }
    }


    //Holder for loading animation
    inner class ViewHolderLoadingAnimate(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var loadingAnimate: ComposeView

        init {
            loadingAnimate = itemView.findViewById(R.id.animate_loading)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val v: View
        return when (viewType) {
            MATCH -> {
                v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.custom_matchs, parent, false)
                ViewHolderMatch(v)
            }
            NEWS -> {
                v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.custom_news, parent, false)
                ViewHolderNews(v, mListener)
            }
            COMPETITION -> {
                v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.custom_competition, parent, false)
                ViewHolderCompetition(v)
            }
            else -> {
                v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.compose_animate_loading, parent, false)
                ViewHolderLoadingAnimate(v)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {

            MATCH -> {
                holder as ViewHolderMatch

                val layoutManager = LinearLayoutManager(
                    holder.itemView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                val adapter = MatchHomeAdapter()
                adapter.listMatch = listMatch

                holder.itemMatchRecyclerView.layoutManager = layoutManager
                holder.itemMatchRecyclerView.adapter = adapter

                holder.itemNoInternet.visibility =
                    if (Global.internet)
                        View.GONE
                    else{
                        adapter.listMatch = mutableListOf()
                        View.VISIBLE
                    }

            }

            NEWS -> {
                holder as ViewHolderNews

                if (listNews.size == 0)
                    return

                var positionListNews = 0
                when (position) {
                    in 1..3 -> positionListNews = position - 1
                    in 5..itemCount -> positionListNews = position - 2
                }

                val news: Content = listNews[positionListNews]

                holder.customNews.setTitle(news.title)
                holder.customNews.setTime(Helpers.CalculateDistanceTime(news.date))

                if (news.bitmapAvatar != null) {
                    holder.customNews.setAvatarBitmap(news.bitmapAvatar!!)
                }

                if (news.bitmapLogo != null) {
                    holder.customNews.setLogoBitmap(news.bitmapLogo!!)
                }

                holder.customNews.readyToDraw = true

                holder.customNews.margin(top = 5F, bottom = 5F)

            }

            COMPETITION -> {
                holder as ViewHolderCompetition
                val layoutManager = LinearLayoutManager(
                    holder.itemView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                val adapter = CompetitionHomeAdapter()
                adapter.listCompetition = listCompetition

                holder.itemCompetitionRecyclerView.layoutManager = layoutManager
                holder.itemCompetitionRecyclerView.adapter = adapter


                holder.itemNoInternet.visibility =
                    if (Global.internet)
                        View.GONE
                    else
                    {
                        adapter.listCompetition = mutableListOf()
                        View.VISIBLE
                    }

            }

            LOADING -> {
                holder as ViewHolderLoadingAnimate

                holder.loadingAnimate.setContent {

                    MaterialTheme {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LoadingAnimation()
                        }

                    }
                }
            }

        }
    }


}