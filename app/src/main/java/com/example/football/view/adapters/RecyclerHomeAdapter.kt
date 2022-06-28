package com.example.football.view.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import java.io.File

class RecyclerHomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


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
        return listNews.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 1
        else {
            if (position==4) 3 else 2
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
                listener.onItemClick(listNews.get(adapterPosition).content_id)
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        var v : View
        return when (viewType) {
            1 -> {
                v = LayoutInflater.from(parent.context).inflate(R.layout.custom_matchs, parent, false)
                ViewHolderMatch(v)
            }
            2 -> {
                v = LayoutInflater.from(parent.context).inflate(R.layout.custom_news, parent, false)
                ViewHolderNews(v, mListener)
            }
            else -> {
                v = LayoutInflater.from(parent.context).inflate(R.layout.custom_competition, parent, false)
                ViewHolderCompetition(v)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> {
                holder as ViewHolderMatch

                val layoutManager = LinearLayoutManager(holder.itemView.context,LinearLayoutManager.HORIZONTAL,false)
                val adapter = RecyclerMatchHomeAdapter()
                adapter.listMatch = listMatch

                holder.itemMatchRecyclerView.layoutManager=layoutManager
                holder.itemMatchRecyclerView.adapter=adapter

            }
            2 -> {
                holder as ViewHolderNews
                val news: Content = listNews.get(position)

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
            3 ->{
                holder as ViewHolderCompetition
                val layoutManager = LinearLayoutManager(holder.itemView.context,LinearLayoutManager.HORIZONTAL,false)
                val adapter = RecyclerCompetitionHomeAdapter()
                adapter.listCompetition = listCompetition

                holder.itemCompetitionRecyclerView.layoutManager=layoutManager
                holder.itemCompetitionRecyclerView.adapter=adapter
            }

        }
    }


}