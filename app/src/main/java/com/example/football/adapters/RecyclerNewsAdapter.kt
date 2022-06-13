package com.example.football.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.football.model.Content
import com.example.football.R
import com.example.football.utils.Helpers
import com.squareup.picasso.Picasso
import java.util.*

class RecyclerNewsAdapter: RecyclerView.Adapter<RecyclerNewsAdapter.ViewHolder>() {


    private lateinit var mListener : onItemClickListener

    var ListNews = mutableListOf<Content>()

    interface onItemClickListener{
        fun onItemClick(idContent: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener=listener
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerNewsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_news,parent,false)
        return ViewHolder(v,mListener)
    }


    override fun onBindViewHolder(holder: RecyclerNewsAdapter.ViewHolder, position: Int) {
        val news : Content = ListNews.get(position)

        holder.itemTime.text = Helpers.CalculateDistanceTime(news.date)

        holder.itemTitle.text = news.title

        if(news.avatar_url.isNotEmpty()) Glide.with(holder.itemView).load(news.avatar_url).diskCacheStrategy(
            DiskCacheStrategy.NONE).centerCrop().into(holder.itemImageNews)
        if(news.publisher_logo.isNotEmpty()) Glide.with(holder.itemView).load(news.publisher_logo).into(holder.itemLogo)


    }

    override fun getItemCount(): Int {
        return ListNews.size
    }

    inner class ViewHolder(itemView: View,listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
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
                listener.onItemClick(ListNews.get(adapterPosition).content_id)
            }
        }

    }


}