package com.example.dragapp.fragments.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dragapp.api.RetrofitInstance
import com.example.dragapp.api.RetrofitInterceptor
import com.example.dragapp.databinding.FragmentProfileBinding
import com.example.dragapp.repositories.DragRepository
import com.example.dragapp.utils.Constants.Companion.BASE_URL
import com.example.dragapp.viewmodels.AppViewModel
import com.example.dragapp.viewmodels.DragViewModel
import com.example.dragapp.viewmodels.DragViewModelFactory

class ProfileFragment : Fragment() {

    private lateinit var mDragViewModel: DragViewModel
    private lateinit var mAppViewModel: AppViewModel

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var tokenFromDataStore = "none"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // data binding
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        // set so ViewModel livedata can be observed from layout
        //binding.lifecycleOwner = this // works only with databinding layout

        // DragViewModel init
        val dragRepository = DragRepository()
        val dragViewModelFactory = DragViewModelFactory(dragRepository)
        mDragViewModel = ViewModelProvider(this, dragViewModelFactory).get(DragViewModel::class.java)

        mAppViewModel = ViewModelProvider(this).get(AppViewModel::class.java)


        mAppViewModel.readFromDataStore.observe(viewLifecycleOwner, Observer { myToken ->
            tokenFromDataStore = myToken
            Log.d("PROFILE: data store token:", tokenFromDataStore)

        })


        mDragViewModel.currentUser()

        mDragViewModel.currentUserData.observe(viewLifecycleOwner, Observer { response ->
            if(response.isSuccessful) {
                Log.d("Profile BODY:", response.body()?.data.toString())
                binding.userNameTv.text =  response.body()?.data?.name.toString()
                binding.userEmailTv.text =  response.body()?.data?.email.toString()

                response.body()?.data?.let {
                    val imageUrl = BASE_URL + it.image
                    Glide.with(this)
                        .load(imageUrl)
                        .circleCrop()
                        .into(binding.userImageIv)
                }


            }else{
                Log.d("Profile Error:", response.errorBody().toString())
                Log.d("Profile interceptor:", RetrofitInterceptor.authToken)

            }
        })


        return binding.root

    }

}