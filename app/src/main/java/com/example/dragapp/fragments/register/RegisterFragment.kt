package com.example.dragapp.fragments.register

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
import com.example.dragapp.databinding.FragmentRegisterBinding
import com.example.dragapp.models.Register
import com.example.dragapp.repositories.DragRepository
import com.example.dragapp.viewmodels.AppViewModel
import com.example.dragapp.viewmodels.DragViewModel
import com.example.dragapp.viewmodels.DragViewModelFactory

class RegisterFragment : Fragment() {

    private lateinit var mDragViewModel: DragViewModel
    private lateinit var mAppViewModel: AppViewModel

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // data binding
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        // set so ViewModel livedata can be observed from layout
        //binding.lifecycleOwner = this // works only with databinding layout

        // DragViewModel init
        val dragRepository = DragRepository()
        val dragViewModelFactory = DragViewModelFactory(dragRepository)
        mDragViewModel = ViewModelProvider(this, dragViewModelFactory).get(DragViewModel::class.java)

        mAppViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        binding.registerButton.setOnClickListener {
            val name = binding.registerNameEt.text.toString()
            val email = binding.registerEmailEt.text.toString()
            val password = binding.registerPasswordEt.text.toString()

            val registerData = Register(name, email, password)
            mDragViewModel.register(registerData)
            mDragViewModel.registerData.observe(viewLifecycleOwner, Observer {  response ->
                if(response.isSuccessful){
                    val token = response.body()?.token.toString()
                    val tokenString = "Bearer $token"

                    Log.d("register tokenString:", tokenString)

                    // save token to data store
                    mAppViewModel.saveToDataStore(tokenString)

                    // navigate to onboarding
                    findNavController().navigate(R.id.action_registerFragment_to_viewPagerFragment)

                }else{
                    // Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show()

                }
            })
        }


        return binding.root
    }

}