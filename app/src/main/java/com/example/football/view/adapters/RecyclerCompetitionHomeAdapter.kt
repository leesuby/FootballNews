package com.example.football.view.adapters

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
import com.example.football.R
import com.example.football.data.model.SoccerCompetition
import com.example.football.utils.Helpers

class RecyclerCompetitionHomeAdapter : RecyclerView.Adapter<RecyclerCompetitionHomeAdapter.CompetitionHomeViewHolder>(){

    var listCompetition = mutableListOf<SoccerCompetition>()

    inner class CompetitionHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var itemLogoCompetition: ImageView
        var itemNameCompetition: TextView

        init {
            itemLogoCompetition = itemView.findViewById(R.id.img_competitionHome)
            itemNameCompetition = itemView.findViewById(R.id.tv_competitionHome)

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompetitionHomeViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.custom_homecompetition,parent,false)
        return CompetitionHomeViewHolder(view)
    }


    override fun onBindViewHolder(holder: CompetitionHomeViewHolder, position: Int) {
        var competition = listCompetition[position]

        Helpers.checkandLoadImageGlide(competition.competition_logo,holder.itemLogoCompetition,holder.itemView.context)
        holder.itemNameCompetition.text = competition.competition_name
    }


    override fun getItemCount(): Int {
        return listCompetition.size
    }
}