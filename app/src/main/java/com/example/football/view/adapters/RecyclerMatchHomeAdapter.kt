package com.example.football.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.football.R
import com.example.football.data.model.SoccerMatch
import com.example.football.utils.Helpers.Companion.margin

class RecyclerMatchHomeAdapter : RecyclerView.Adapter<RecyclerMatchHomeAdapter.MatchViewHolder>() {

    var listMatch = mutableListOf<SoccerMatch>()

    inner class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemMatchLine : View
        var itemCompetitionName : TextView
        var itemTime : TextView
        var itemTeam1Name: TextView
        var itemTeam2Name: TextView
        var itemTeam1Logo: ImageView
        var itemTeam2Logo: ImageView

        init{
            itemMatchLine = itemView.findViewById(R.id.match_lineHome)
            itemCompetitionName = itemView.findViewById(R.id.tv_matchnameHome)
            itemTime = itemView.findViewById(R.id.tv_matchtime)
            itemTeam1Name = itemView.findViewById(R.id.tv_nameteam1Home)
            itemTeam1Logo = itemView.findViewById(R.id.img_matchteam1Home)
            itemTeam2Name = itemView.findViewById(R.id.tv_nameteam2Home)
            itemTeam2Logo = itemView.findViewById(R.id.img_matchteam2Home)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_homematch,parent,false)
        return MatchViewHolder(v)
    }


    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = listMatch.get(position)
        if(position!=0)
        holder.itemView.margin(left = 10f)

    }


    override fun getItemCount(): Int {
        return listMatch.size
    }
}