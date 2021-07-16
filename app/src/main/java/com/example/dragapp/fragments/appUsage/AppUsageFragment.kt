package com.example.dragapp.fragments.appUsage

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dragapp.databinding.FragmentAppUsageBinding
import com.example.dragapp.models.AppUsageAdapter
import com.example.dragapp.models.AppUsageModel


class AppUsageFragment : Fragment() {

    private var _binding: FragmentAppUsageBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentAppUsageBinding.inflate(inflater, container, false)
        val appUsageModel = AppUsageModel()

        if (appUsageModel.checkUsageStatePermission()) {
            appUsageModel.getUsageStatsSocialAppsDay()
            appUsageModel.groupList()
            val layoutManager = LinearLayoutManager(activity)
            binding.appUsageRecycler.layoutManager = layoutManager
            binding.appUsageRecycler.adapter = AppUsageAdapter(AppUsageModel.appUsageSelected)
        } else {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
        return view
    }

}