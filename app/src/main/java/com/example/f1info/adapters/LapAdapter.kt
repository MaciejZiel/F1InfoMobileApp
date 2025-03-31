package com.example.f1info.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.f1info.R
import com.example.f1info.models.Lap

class LapAdapter(private val laps: List<Lap>) : RecyclerView.Adapter<LapAdapter.LapViewHolder>() {

    class LapViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val driverName: TextView = view.findViewById(R.id.tvDriverName)
        val lapTime: TextView = view.findViewById(R.id.tvLapTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lap, parent, false)
        return LapViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        val lap = laps[position]
        holder.driverName.text = lap.driverNumber
        holder.lapTime.text = lap.lapTime
    }


    override fun getItemCount(): Int = laps.size
}
