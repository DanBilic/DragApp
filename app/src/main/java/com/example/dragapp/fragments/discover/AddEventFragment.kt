package com.example.dragapp.fragments.discover

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.dragapp.R

class AddEventFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // set menu
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_event_menu, menu)
    }
}