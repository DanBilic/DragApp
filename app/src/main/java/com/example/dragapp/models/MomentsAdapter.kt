package com.example.dragapp.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.dragapp.R
import com.example.dragapp.utils.MomentsData

class MomentsAdapter(private val dataSet: List<MomentsData>) :
    RecyclerView.Adapter<MomentsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var username: TextView = view.findViewById(R.id.taskTitle)
        var text: TextView = view.findViewById(R.id.taskDescription)
        var posttime: TextView = view.findViewById(R.id.posttimeView)
        var pictureView: ImageView = view.findViewById(R.id.picture1)
        var profilepicture: ImageView = view.findViewById(R.id.profilepicture)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_moments_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = dataSet[position]
        holder.username.text = user.username
        holder.text.text = user.text
        holder.posttime.text = user.posttime
        if (user.picture != "") {
            holder.pictureView.setImageBitmap(decode(user.picture))
        }
        if (user.profilepicture != "") {
            holder.profilepicture.setImageBitmap(decode(user.profilepicture))
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    private fun decode(string: String): Bitmap {

        val imageBytes: ByteArray = Base64.decode(string, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}
