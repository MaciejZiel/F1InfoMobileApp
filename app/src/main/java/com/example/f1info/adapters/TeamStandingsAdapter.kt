// TeamStandingsAdapter.kt
package com.example.f1info.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.f1info.R
import com.example.f1info.databinding.ItemTeamStandingBinding
import com.example.f1info.models.ConstructorStanding

class TeamStandingsAdapter : ListAdapter<ConstructorStanding, TeamStandingsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTeamStandingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val constructor = getItem(position)
        holder.binding.tvPosition.text = constructor.position.toString()
        holder.binding.tvTeamName.text = constructor.constructor_name
        holder.binding.tvPoints.text = "${constructor.points.toInt()} pts"

        val teamColors = mapOf(
            "RED BULL RACING" to ContextCompat.getColor(holder.binding.tvPosition.context, R.color.redbull_blue),
            "MERCEDES" to ContextCompat.getColor(holder.binding.tvPosition.context, R.color.mercedes_teal),
            "FERRARI" to ContextCompat.getColor(holder.binding.tvPosition.context, R.color.ferrari_red),
            "MCLAREN" to ContextCompat.getColor(holder.binding.tvPosition.context, R.color.mclaren_orange),
            "ASTON MARTIN" to ContextCompat.getColor(holder.binding.tvPosition.context, R.color.aston_green),
            "HAAS" to ContextCompat.getColor(holder.binding.tvPosition.context, R.color.haas_gray),
            "WILLIAMS" to ContextCompat.getColor(holder.binding.tvPosition.context, R.color.williams_blue),
            "ALPINE" to ContextCompat.getColor(holder.binding.tvPosition.context, R.color.alpine_blue),
            "KICK SAUBER" to ContextCompat.getColor(holder.binding.tvPosition.context, R.color.sauber_green),
            "RACING BULLS" to ContextCompat.getColor(holder.binding.tvPosition.context, R.color.rb_violet)
        )

        val teamColor = teamColors[constructor.constructor_name.uppercase()] ?: ContextCompat.getColor(holder.binding.tvPosition.context, R.color.black)

        val drawable = ContextCompat.getDrawable(holder.binding.tvPosition.context, R.drawable.circle_background) as GradientDrawable
        drawable.setColor(teamColor)
        holder.binding.tvPosition.background = drawable
    }


    class ViewHolder(internal val binding: ItemTeamStandingBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    private class DiffCallback : DiffUtil.ItemCallback<ConstructorStanding>() {
        override fun areItemsTheSame(oldItem: ConstructorStanding, newItem: ConstructorStanding): Boolean {
            return oldItem.constructor_name == newItem.constructor_name
        }

        override fun areContentsTheSame(oldItem: ConstructorStanding, newItem: ConstructorStanding): Boolean {
            return oldItem == newItem
        }
    }
}
