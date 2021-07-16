package com.example.dragapp.fragments.appUsage

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dragapp.R
import com.example.dragapp.databinding.ActivityDashboardBinding.inflate
import com.example.dragapp.models.AppUsageModel


class AppUsageFragment : Fragment() {

    //Todo does not work:
    private lateinit var recyclerView: RecyclerView
    //private var _binding: AppUsageFragment? = null
    // private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        //_binding = AppUsageFragment.inflate(inflater, container, false)
        val view = inflater.inflate(R.layout.app_usage_card, container, false)
        val appUsageModel = AppUsageModel()

        if (appUsageModel.checkUsageStatePermission()) {
            appUsageModel.getUsageStatsSocialAppsDay()
            appUsageModel.groupList()
            recyclerView = view.findViewById(R.id.appUsage_recycler)
            val layoutManager = LinearLayoutManager(activity)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = AppUsageAdapter(AppUsageModel.appUsageSelected)
        } else {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
        return view
    }

}