package com.example.dragapp.fragments.moments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dragapp.R
import com.example.dragapp.databinding.FragmentLoginBinding
import com.example.dragapp.databinding.FragmentMomentsBinding
import com.example.dragapp.models.MomentsAdapter
import kotlinx.android.synthetic.main.fragment_moments.view.*

class MomentsFragment : Fragment() {

    //private lateinit var momentsDataConnection: MomentsDataConnection
    private lateinit var momentsAdapter: MomentsAdapter
    private var _binding: FragmentMomentsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Hide titleBar
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        // Inflate the layout for this fragment
        _binding = FragmentMomentsBinding.inflate(inflater, container, false)

        binding.composeButton.setOnClickListener {
            findNavController().navigate(R.id.action_momentsFragment_to_composeMomentsFragment)
        }
        /*val momentsNetworkCall = MomentsNetworkCall()
        val momentsConnectionFactory = MomentsConnectionFactory(momentsNetworkCall)
        momentsDataConnection = ViewModelProvider(this, momentsConnectionFactory).get(
            MomentsDataConnection::class.java)

        momentsDataConnection.getMoments()
        momentsDataConnection.getMoments.observe(viewLifecycleOwner, { response  ->*/
            //momentsAdapter = MomentsAdapter(response!!)
            val layoutManager = LinearLayoutManager(activity)
            binding.momentsRecycler.layoutManager = layoutManager
            binding.momentsRecycler.itemAnimator = DefaultItemAnimator()
            binding.momentsRecycler.adapter = momentsAdapter
        //})
        return view
    }
}