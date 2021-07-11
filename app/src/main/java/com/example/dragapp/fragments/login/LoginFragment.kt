package com.example.dragapp.fragments.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dragapp.R
import com.example.dragapp.databinding.FragmentLoginBinding
import com.example.dragapp.repositories.DragRepository
import com.example.dragapp.viewmodels.DragViewModel
import com.example.dragapp.viewmodels.DragViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {

    private lateinit var mDragViewModel: DragViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // data binding
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        // set so ViewModel livedata can be observed from layout
        //binding.lifecycleOwner = this // works only with databinding layout

        // DragViewModel init
        val dragRepository = DragRepository()
        val dragViewModelFactory = DragViewModelFactory(dragRepository)
        mDragViewModel = ViewModelProvider(this, dragViewModelFactory).get(DragViewModel::class.java)
        mDragViewModel.login()
        mDragViewModel.loginData.observe(viewLifecycleOwner, Observer { response ->
            if(response.isSuccessful){
                Log.d("Body:", response.body().toString())
                Log.d("Headers:", response.headers().toString())
            }else{
                Log.d("Body:", response.body().toString())
            }
        })

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }



        return binding.root;
    }

}