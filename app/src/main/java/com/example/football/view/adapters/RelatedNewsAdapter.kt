package com.example.football.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.football.data.model.Content
import com.example.football.R
import com.example.football.utils.Helpers
import com.example.football.utils.View.margin
import com.example.football.view.customview.NewsCustomView

class RelatedNewsAdapter: RecyclerView.Adapter<RelatedNewsAdapter.ViewHolder>() {


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
    ): RelatedNewsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_news,parent,false)
        return ViewHolder(v,mListener)
    }


    override fun onBindViewHolder(holder: RelatedNewsAdapter.ViewHolder, position: Int) {
        val news : Content = listNews.get(position)


        holder.customNews.setTitle(news.title)
        holder.customNews.setTime(Helpers.CalculateDistanceTime(news.date))

        if (news.bitmapAvatar != null ) {
            holder.customNews.setAvatarBitmap(news.bitmapAvatar!!)
        }

        if(news.bitmapLogo !=null ){
            holder.customNews.setLogoBitmap(news.bitmapLogo!!)
        }

        holder.customNews.readyToDraw = true
        holder.customNews.margin(bottom = 10F)





    }

    override fun getItemCount(): Int {
        return listNews.size
    }

    inner class ViewHolder(itemView: View,listener: onNewsClickListener) : RecyclerView.ViewHolder(itemView) {
            var customNews: NewsCustomView

        init {

            customNews = itemView.findViewById(R.id.custom_news)
            itemView.setOnClickListener{
                listener.onItemClick(listNews.get(adapterPosition).contentId)
            }
        }

    }

    fun setNewsList(newNewsList: MutableList<Content>){
        val diffUtil = NewsHomeDiffUtil(listNews,newNewsList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        listNews = newNewsList
        diffResult.dispatchUpdatesTo(this)
    }


}