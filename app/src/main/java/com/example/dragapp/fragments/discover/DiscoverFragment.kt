package com.example.dragapp.fragments.discover

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dragapp.R
import kotlinx.android.synthetic.main.fragment_discover.view.*

class DiscoverFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_discover, container, false)

        view.event_add_button.setOnClickListener{
            findNavController().navigate(R.id.action_discoverFragment_to_addEventFragment)
        }

        return view
    }

}