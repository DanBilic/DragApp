package com.example.dragapp.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dragapp.R
import com.google.android.material.chip.Chip

class ChipAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<ChipAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var chip: Chip = view.findViewById(R.id.chip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chip_element, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.chip.text = dataSet[position]
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}