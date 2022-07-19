package com.example.football.view.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.football.data.model.Content

//Difutil for check difference data on list news Home
class NewsHomeDiffUtil(private val oldNewsList: MutableList<Content>,
                       private val newNewsList: MutableList<Content>) :
    DiffUtil.Callback() {

    //get size of old list news
    override fun getOldListSize(): Int {
        return oldNewsList.size
    }

    //get size of new list news
    override fun getNewListSize(): Int {
        return newNewsList.size
    }

    //check if two item are the same
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNewsList[oldItemPosition].contentId == newNewsList[newItemPosition].contentId
    }

    //check if content of item are the same
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNewsList[oldItemPosition].contentId == newNewsList[newItemPosition].contentId
    }
}