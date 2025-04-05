package com.example.f1info.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.f1info.DriverDetailsActivity
import com.example.f1info.R
import com.example.f1info.databinding.ItemDriverStandingBinding
import com.example.f1info.models.DriverStanding

class DriverStandingsAdapter :
    ListAdapter<DriverStanding, DriverStandingsAdapter.DriverViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val binding = ItemDriverStandingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DriverViewHolder(binding)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val driver = getItem(position)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DriverDetailsActivity::class.java).apply {
                putExtra("driver_name", "${driver.name} ${driver.surname}")
                putExtra("team_name", driver.team)
                putExtra("points", driver.points)
                putExtra("image_url", driver.picture_url)
                putExtra("base_podiums", driver.basePodiums)
            }
            context.startActivity(intent)
        }

        holder.binding.tvDriverName.text = "${driver.name} ${driver.surname}"
        holder.binding.tvTeamName.text = driver.team
        holder.binding.tvPoints.text = "${driver.points} pts"

        val teamLogos = mapOf(
            "RED BULL RACING" to R.drawable.redbull_logo_standing,
            "MERCEDES" to R.drawable.mercedes_logo_standing,
            "FERRARI" to R.drawable.ferrari_logo,
            "MCLAREN" to R.drawable.mclaren_logo_standing,
            "ASTON MARTIN" to R.drawable.aston_martin_logo_standing,
            "HAAS" to R.drawable.haas_logo_standing,
            "WILLIAMS" to R.drawable.williams_logo_standing,
            "ALPINE" to R.drawable.alpine_logo_standing,
            "KICK SAUBER" to R.drawable.kick_sauber_logo,
            "RACING BULLS" to R.drawable.rb_logo_standing
        )

        val teamLogoRes = teamLogos[driver.team.uppercase()]
        holder.binding.ivTeamLogo.setImageResource(teamLogoRes ?: R.drawable.f1_logo)
    }


    class DriverViewHolder(val binding: ItemDriverStandingBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback : DiffUtil.ItemCallback<DriverStanding>() {
        override fun areItemsTheSame(oldItem: DriverStanding, newItem: DriverStanding): Boolean {
            return oldItem.surname == newItem.surname && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: DriverStanding, newItem: DriverStanding): Boolean {
            return oldItem == newItem
        }
    }
}
