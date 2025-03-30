
// DriverStandingsAdapter.kt
package com.example.f1info.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.f1info.databinding.ItemDriverStandingBinding
import com.example.f1info.models.DriverStanding

class DriverStandingsAdapter : ListAdapter<DriverStanding, DriverStandingsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDriverStandingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemDriverStandingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DriverStanding) {
            binding.tvPosition.text = item.position.toString()
            binding.tvDriverName.text = item.driver_full_name
            binding.tvTeamName.text = item.constructor_name
            binding.tvPoints.text = item.points.toString()
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<DriverStanding>() {
        override fun areItemsTheSame(oldItem: DriverStanding, newItem: DriverStanding): Boolean {
            return oldItem.driver_number == newItem.driver_number
        }

        override fun areContentsTheSame(oldItem: DriverStanding, newItem: DriverStanding): Boolean {
            return oldItem == newItem
        }
    }
}
