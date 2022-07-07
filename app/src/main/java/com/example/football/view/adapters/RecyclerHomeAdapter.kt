package com.example.football.view.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.football.data.model.Content
import com.example.football.R
import com.example.football.data.model.Competition
import com.example.football.data.model.SoccerCompetition
import com.example.football.data.model.SoccerMatch
import com.example.football.utils.Helpers
import com.example.football.view.compose.LoadingAnimation
import java.io.File

class RecyclerHomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MATCH = 0
    private val NEWS = 1
    private val COMPETITION = 2
    private val LOADING = 3

    private lateinit var mListener: onNewsClickListener

    var listNews = mutableListOf<Content>()
    var listMatch = mutableListOf<SoccerMatch>()
    var listCompetition = mutableListOf<SoccerCompetition>()

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
        return when(position){
            0 -> MATCH
            4 -> COMPETITION
            itemCount - 1-> LOADING
            else -> NEWS
        }
    }

    inner class ViewHolderNews(itemView: View, listener: onNewsClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var itemImageNews: ImageView
        var itemTitle: TextView
        var itemLogo: ImageView
        var itemTime: TextView

        init {
            itemImageNews = itemView.findViewById(R.id.img_news)
            itemTitle = itemView.findViewById(R.id.txv_title)
            itemLogo = itemView.findViewById(R.id.img_logo)
            itemTime = itemView.findViewById(R.id.tv_time)

            itemView.setOnClickListener {
                var positionListNews: Int = 0
                when(adapterPosition){
                    in 1..3 -> positionListNews = adapterPosition - 1
                    in 5..itemCount -> positionListNews = adapterPosition -2
                }

                listener.onItemClick(listNews.get(positionListNews).content_id)
            }
        }

    }

    inner class ViewHolderMatch(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var itemMatchRecyclerView: RecyclerView

        init{
           itemMatchRecyclerView = itemView.findViewById(R.id.RV_homeMatch)
        }

    }

    inner class ViewHolderCompetition(itemView: View) :
        RecyclerView.ViewHolder(itemView){
        var itemCompetitionRecyclerView: RecyclerView

        init {
            itemCompetitionRecyclerView = itemView.findViewById(R.id.RV_homeCompetition)
        }
    }

    inner class ViewHolderLoadingAnimate(itemView: View) :
        RecyclerView.ViewHolder(itemView){
            var loadingAnimate : ComposeView

            init {
                loadingAnimate = itemView.findViewById(R.id.animate_loading)
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        var v : View
        return when (viewType) {
            MATCH -> {
                v = LayoutInflater.from(parent.context).inflate(R.layout.custom_matchs, parent, false)
                ViewHolderMatch(v)
            }
            NEWS -> {
                v = LayoutInflater.from(parent.context).inflate(R.layout.custom_news, parent, false)
                ViewHolderNews(v, mListener)
            }
            COMPETITION -> {
                v = LayoutInflater.from(parent.context).inflate(R.layout.custom_competition, parent, false)
                ViewHolderCompetition(v)
            }
            else -> {
                v = LayoutInflater.from(parent.context).inflate(R.layout.compose_animate_loading, parent, false)
                ViewHolderLoadingAnimate(v)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            MATCH -> {
                holder as ViewHolderMatch

                val layoutManager = LinearLayoutManager(holder.itemView.context,LinearLayoutManager.HORIZONTAL,false)
                val adapter = RecyclerMatchHomeAdapter()
                adapter.listMatch = listMatch

                holder.itemMatchRecyclerView.layoutManager=layoutManager
                holder.itemMatchRecyclerView.adapter=adapter


            }
            NEWS -> {
                holder as ViewHolderNews

                if(listNews.size==0)
                  return

                var positionListNews: Int = 0
                when(position){
                    in 1..3 -> positionListNews = position - 1
                    in 5..itemCount -> positionListNews = position -2
                }

                val news: Content = listNews[positionListNews]


                holder.itemTime.text =
                    Helpers.CalculateDistanceTime(
                        news.date
                    )
                holder.itemTitle.text = news.title ?: "Không có dữ liệu"

                Helpers.checkandLoadImageGlide(news.avatar_url,holder.itemImageNews,holder.itemView.context)

                if (!news.publisher_logo.isNullOrBlank()) {
                    if (Helpers.internet) {
                        Glide.with(holder.itemView).load(news.publisher_logo).into(holder.itemLogo)
                    } else {
                        Glide.with(holder.itemView).load(File(news.publisher_logo))
                            .into(holder.itemLogo)
                    }
                }
            }
            COMPETITION ->{
                holder as ViewHolderCompetition
                val layoutManager = LinearLayoutManager(holder.itemView.context,LinearLayoutManager.HORIZONTAL,false)
                val adapter = RecyclerCompetitionHomeAdapter()
                adapter.listCompetition = listCompetition

                holder.itemCompetitionRecyclerView.layoutManager=layoutManager
                holder.itemCompetitionRecyclerView.adapter=adapter
            }
            LOADING -> {
                holder as ViewHolderLoadingAnimate

                holder.loadingAnimate.setContent {

                    MaterialTheme{
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            LoadingAnimation()
                        }

                    }
                }
            }

        }
    }


}