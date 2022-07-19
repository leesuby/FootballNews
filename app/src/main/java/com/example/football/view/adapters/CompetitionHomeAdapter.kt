package com.example.football.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.football.R
import com.example.football.data.model.SoccerCompetition
import com.example.football.utils.Helpers


//Adapter for competition on home page
class CompetitionHomeAdapter : RecyclerView.Adapter<CompetitionHomeAdapter.CompetitionHomeViewHolder>(){

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_homecompetition,parent,false)
        return CompetitionHomeViewHolder(view)
    }


    override fun onBindViewHolder(holder: CompetitionHomeViewHolder, position: Int) {
        val competition = listCompetition[position]

        Helpers.checkandLoadImageGlide(competition.competitionLogo,holder.itemLogoCompetition,holder.itemView.context)
        holder.itemNameCompetition.text = competition.competitionName
    }


    override fun getItemCount(): Int {
        return listCompetition.size
    }
}