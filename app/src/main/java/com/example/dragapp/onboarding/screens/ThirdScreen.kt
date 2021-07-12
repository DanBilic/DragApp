package com.example.dragapp.onboarding.screens

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dragapp.DashboardActivity
import com.example.dragapp.R
import kotlinx.android.synthetic.main.fragment_third_screen.view.*

class ThirdScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_third_screen, container, false)


        view.finish_button_ts.setOnClickListener {

            // navigate to dashboard
            val intent = Intent(activity, DashboardActivity::class.java)
            activity?.startActivity(intent)
            //findNavController().navigate(R.id.action_viewPagerFragment_to_dashboardFragment)
        }

        return view
    }
}