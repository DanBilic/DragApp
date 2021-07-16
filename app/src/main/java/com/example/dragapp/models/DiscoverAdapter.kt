package com.example.dragapp.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.dragapp.R
import com.example.dragapp.utils.MicroTaskData


class DiscoverAdapter (private val dataSet: List<MicroTaskData>) :
    RecyclerView.Adapter<DiscoverAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var taskTitle: TextView = view.findViewById(R.id.taskTitle)
        var taskDescription: TextView = view.findViewById(R.id.taskDescription)
        var taskCategory: TextView = view.findViewById(R.id.category)
        var type:TextView = view.findViewById(R.id.type)
        var taskPicture: ImageView = view.findViewById(R.id.typepicture)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_layout, parent, false)
        println(dataSet)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val microtasks = dataSet[position]
        holder.taskTitle.text = microtasks.Title
        holder.taskDescription.text = microtasks.Description
        holder.taskCategory.text = microtasks.Category
        if (microtasks.Type == "Event") {
            holder.type.text = "Event"
            holder.taskPicture.setImageResource(R.drawable.event)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
