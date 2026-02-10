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
                putExtra("driver_id", driver.driverId)
                putExtra("driver_number", driver.driverNumber ?: -1)
                putExtra("championship_position", driver.position)
            }
            context.startActivity(intent)
        }

        holder.binding.tvDriverName.text = "${driver.name} ${driver.surname}"
        holder.binding.tvTeamName.text = driver.team
        val pointsSuffix = holder.itemView.context.getString(R.string.points_abbrev)
        holder.binding.tvPoints.text = "${formatPoints(driver.points)} $pointsSuffix"

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

    private fun formatPoints(points: Double): String {
        return if (points % 1.0 == 0.0) {
            points.toInt().toString()
        } else {
            points.toString()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DriverStanding>() {
        override fun areItemsTheSame(oldItem: DriverStanding, newItem: DriverStanding): Boolean {
            return oldItem.surname == newItem.surname && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: DriverStanding, newItem: DriverStanding): Boolean {
            return oldItem == newItem
        }
    }
}
