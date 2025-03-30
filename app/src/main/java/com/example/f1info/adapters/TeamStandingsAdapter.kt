// TeamStandingsAdapter.kt
package com.example.f1info.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemTeamStandingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ConstructorStanding) {
            binding.tvPosition.text = item.position.toString()
            binding.tvTeamName.text = item.constructor_name
            binding.tvPoints.text = item.points.toString()
        }
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
