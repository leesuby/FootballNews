package com.example.football.view.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.football.data.model.Content

class NewsHomeDiffUtil(private val oldNewsList: MutableList<Content>,
                       private val newNewsList: MutableList<Content>) :
    DiffUtil.Callback() {


    override fun getOldListSize(): Int {
        return oldNewsList.size
    }

    override fun getNewListSize(): Int {
        return newNewsList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNewsList[oldItemPosition].content_id == newNewsList[newItemPosition].content_id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNewsList[oldItemPosition].content_id == newNewsList[newItemPosition].content_id
    }
}