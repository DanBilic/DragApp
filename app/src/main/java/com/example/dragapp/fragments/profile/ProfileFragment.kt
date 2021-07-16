package com.example.dragapp.fragments.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dragapp.api.RetrofitInterceptor
import com.example.dragapp.databinding.FragmentProfileBinding
import com.example.dragapp.repositories.DragRepository
import com.example.dragapp.services.AskPermissions
import com.example.dragapp.utils.Constants
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

        val askPermission = AskPermissions(requireContext(), requireActivity())
        askPermission.setupGalleryPermissions()

        // DragViewModel init
        val dragRepository = DragRepository()
        val dragViewModelFactory = DragViewModelFactory(dragRepository)
        mDragViewModel = ViewModelProvider(this, dragViewModelFactory).get(DragViewModel::class.java)

        mAppViewModel = ViewModelProvider(this).get(AppViewModel::class.java)


        /*mAppViewModel.readFromDataStore.observe(viewLifecycleOwner, Observer { myToken ->
            tokenFromDataStore = myToken
            Log.d("PROFILE: data store token:", tokenFromDataStore)

        })

        mDragViewModel.currentUser()

        mDragViewModel.currentUserData.observe(viewLifecycleOwner, Observer { response ->
            if(response.isSuccessful) {
                Log.d("Profile Body:", response.body().toString())
                Log.d("Profile Headers:", response.headers().toString())
            }else{
                Log.d("Profile Error:", response.errorBody().toString())
                Log.d("Profile interceptor:", RetrofitInterceptor.authToken)

            }
        })*/

        binding.profilePictureCiv.setOnClickListener{
            selectImageInAlbum()
        }

        return binding.root

    }

    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE) {
            val imageUri = data?.data
            binding.profilePictureCiv.setImageURI(imageUri)
            //saveToDatabase()

        }
    }

}