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
import com.example.football.R
import com.example.football.data.model.SoccerMatch
import com.example.football.utils.Helpers
import com.example.football.utils.Helpers.Companion.margin
import java.io.File
import java.text.SimpleDateFormat

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


    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = listMatch[position]

        if(position!=0)
            holder.itemView.margin(left = 10f)

        holder.itemCompetitionName.text = match.competition.competition_name
        holder.itemTeam1Name.text = match.home_team.team_name
        holder.itemTeam2Name.text = match.away_team.team_name
        var convert = SimpleDateFormat("HH:mm dd MMM yyyy")



        if (!match.home_team.team_logo.isNullOrBlank())
        {
            if(Helpers.internet){
                Glide.with(holder.itemView)
                    .load(match.home_team.team_logo)
                    .apply(
                        RequestOptions
                            .bitmapTransform(RoundedCorners(20))
                            .error(R.drawable.ic_launcher_background)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                    )
                    .into(holder.itemTeam1Logo)
            }
            else{
                Glide.with(holder.itemView)
                    .load(match.home_team.team_logo)
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                    .into(holder.itemTeam1Logo)
            }
        }

        if(!match.away_team.team_logo.isNullOrBlank()){
            if(Helpers.internet){
                Glide.with(holder.itemView)
                    .load(match.away_team.team_logo)
                    .apply(
                        RequestOptions
                            .bitmapTransform(RoundedCorners(20))
                            .error(R.drawable.ic_launcher_background)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                    )
                    .into(holder.itemTeam2Logo)
            }
            else{
                Glide.with(holder.itemView)
                    .load(match.away_team.team_logo)
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                    .into(holder.itemTeam2Logo)
            }
        }


    }


    override fun getItemCount(): Int {
        return listMatch.size
    }
}