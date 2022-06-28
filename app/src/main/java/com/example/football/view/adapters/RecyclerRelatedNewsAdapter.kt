package com.example.football.view.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.football.data.model.Content
import com.example.football.R
import com.example.football.utils.Helpers
import java.io.File

class RecyclerRelatedNewsAdapter: RecyclerView.Adapter<RecyclerRelatedNewsAdapter.ViewHolder>() {


    private lateinit var mListener : onNewsClickListener

    var listNews = mutableListOf<Content>()

    interface onNewsClickListener{
        fun onItemClick(idContent: Int)
    }

    fun setOnItemClickListener(listener: onNewsClickListener){
        mListener=listener
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerRelatedNewsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_news,parent,false)
        return ViewHolder(v,mListener)
    }


    override fun onBindViewHolder(holder: RecyclerRelatedNewsAdapter.ViewHolder, position: Int) {
        val news : Content = listNews.get(position)

        holder.itemTime.text = if (news.date == null) "Không có dữ liệu" else Helpers.CalculateDistanceTime(news.date)
        holder.itemTitle.text = news.title ?: "Không có dữ liệu"

        Helpers.checkandLoadImageGlide(news.avatar_url,holder.itemImageNews,holder.itemView.context)

        if(!news.publisher_logo.isNullOrBlank()){
            if(Helpers.internet){
                Glide.with(holder.itemView).load(news.publisher_logo).into(holder.itemLogo)
            }
            else{
                Glide.with(holder.itemView).load(File(news.publisher_logo)).into(holder.itemLogo)
            }
        }





    }

    override fun getItemCount(): Int {
        return listNews.size
    }

    inner class ViewHolder(itemView: View,listener: onNewsClickListener) : RecyclerView.ViewHolder(itemView) {
        var itemImageNews : ImageView
        var itemTitle : TextView
        var itemLogo : ImageView
        var itemTime : TextView

        init {
            itemImageNews = itemView.findViewById(R.id.img_news)
            itemTitle = itemView.findViewById(R.id.txv_title)
            itemLogo = itemView.findViewById(R.id.img_logo)
            itemTime = itemView.findViewById(R.id.tv_time)

            itemView.setOnClickListener{
                listener.onItemClick(listNews.get(adapterPosition).content_id)
            }
        }

    }


}