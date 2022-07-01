package com.example.football.view.adapters

import android.annotation.SuppressLint
import android.graphics.Color
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
import java.util.*

class RecyclerMatchHomeAdapter : RecyclerView.Adapter<RecyclerMatchHomeAdapter.MatchViewHolder>() {

    private val MATCH_DONE = 0
    private val MATCH_GOING = 1
    private val MATCH_COMING = 2
    var listMatch = mutableListOf<SoccerMatch>()

    inner class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemMatchLine : View
        var itemCompetitionName : TextView
        var itemTime : TextView
        var itemTeam1Name: TextView
        var itemTeam2Name: TextView
        var itemTeam1Logo: ImageView
        var itemTeam2Logo: ImageView
        var itemTeam1Score: TextView
        var itemTeam2Score: TextView

        init{
            itemMatchLine = itemView.findViewById(R.id.match_lineHome)
            itemCompetitionName = itemView.findViewById(R.id.tv_matchnameHome)
            itemTime = itemView.findViewById(R.id.tv_matchtime)
            itemTeam1Name = itemView.findViewById(R.id.tv_nameteam1Home)
            itemTeam1Logo = itemView.findViewById(R.id.img_matchteam1Home)
            itemTeam2Name = itemView.findViewById(R.id.tv_nameteam2Home)
            itemTeam2Logo = itemView.findViewById(R.id.img_matchteam2Home)
            itemTeam1Score = itemView.findViewById(R.id.tv_scoreteam1Home)
            itemTeam2Score = itemView.findViewById(R.id.tv_scoreteam2Home)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_homematch,parent,false)
        return MatchViewHolder(v)
    }


    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = listMatch[position]
        var convertTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
        var convertDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        if(position!=0)
            holder.itemView.margin(left = 10f)

        when(match.match_status){

            MATCH_GOING -> {holder.itemMatchLine.setBackgroundColor(Color.RED)
                holder.itemTime.text= convertTime.format(match.start_time.toLong()) + " " + convertDate.format(match.start_time.toLong())
                holder.itemTeam1Score.text = match.home_scored.toString()
                holder.itemTeam2Score.text = match.away_scored.toString()
            }

            MATCH_COMING -> {holder.itemMatchLine.setBackgroundColor(Color.GREEN)
                holder.itemTime.text= convertTime.format(match.start_time.toLong()) + " " + convertDate.format(match.start_time.toLong())
            }

            MATCH_DONE ->{ holder.itemMatchLine.setBackgroundColor(Color.GRAY)
                holder.itemTime.text = "Kết thúc"
                holder.itemTeam1Score.text = match.home_scored.toString()
                holder.itemTeam2Score.text = match.away_scored.toString()

            }

            else -> {holder.itemMatchLine.setBackgroundColor(Color.LTGRAY)
                holder.itemTime.text= convertTime.format(match.start_time.toLong()) + " " + convertDate.format(match.start_time.toLong())
            }
        }

        holder.itemCompetitionName.text = match.competition.competition_name
        holder.itemTeam1Name.text = match.home_team.team_name
        holder.itemTeam2Name.text = match.away_team.team_name

        Helpers.checkandLoadImageGlide(match.home_team.team_logo,holder.itemTeam1Logo,holder.itemView.context)
        Helpers.checkandLoadImageGlide(match.away_team.team_logo,holder.itemTeam2Logo,holder.itemView.context)




    }


    override fun getItemCount(): Int {
        return listMatch.size
    }
}