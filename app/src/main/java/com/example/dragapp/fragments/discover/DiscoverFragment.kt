package com.example.dragapp.fragments.discover

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dragapp.databinding.FragmentDiscoverBinding

class DiscoverFragment : Fragment() {
    // private lateinit var discoverDataConnection: DiscoverDataConnection
    private lateinit var discoverAdapter: DiscoverAdapter

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Hide titleBar
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        // Inflate the layout for this fragment
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)

        /*val discoverNetworkCall = DiscoverNetworkCall()
        val discoverConnectionFactory = DiscoverConnectionFactory(discoverNetworkCall)
        discoverDataConnection = ViewModelProvider(this, discoverConnectionFactory).get(
            DiscoverDataConnection::class.java)

        discoverDataConnection.getMicroTaskData()
        discoverDataConnection.getMicroTaskData.observe(viewLifecycleOwner, { response  ->
            discoverAdapter = DiscoverAdapter(response!!)*/
            val layoutManager = LinearLayoutManager(activity)
            binding.discoverrecycler.layoutManager = layoutManager
            binding.discoverrecycler.itemAnimator = DefaultItemAnimator()
            binding.discoverrecycler.adapter = discoverAdapter
        // })
        return binding.root
    }


}