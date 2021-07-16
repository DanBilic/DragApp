package com.example.dragapp.fragments.moments

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dragapp.R
import com.example.dragapp.databinding.FragmentComposeMomentsBinding
import com.example.dragapp.services.AskPermissions
import com.example.dragapp.utils.Constants
import com.google.android.material.snackbar.Snackbar

class ComposeMomentsFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var inputView: EditText
    //private lateinit var momentsDataConnection: MomentsDataConnection
    //private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentComposeMomentsBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Hide titleBar
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        // Inflate the layout for this fragment
        _binding = FragmentComposeMomentsBinding.inflate(inflater, container, false)

        /*val momentsNetworkCall = MomentsNetworkCall()
        val viewModelFactory = MomentsConnectionFactory(momentsNetworkCall)
        momentsDataConnection = ViewModelProvider(this, viewModelFactory).get(MomentsDataConnection::class.java)*/

        val askPermission = AskPermissions(requireContext(), requireActivity())
        askPermission.setupGalleryPermissions()
        binding.postButton.setOnClickListener {
           /* val encode = Encode(imageView)
            saveDataIntoDatabase(encode.createImageStringFromBitmap())*/
            Snackbar.make(it,"successful", Snackbar.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_composeMomentsFragment_to_momentsFragment)
        }

        binding.addPictureButton.setOnClickListener {
            selectImageInAlbum()
        }

        //profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        return binding.root
    }

    /*@RequiresApi(Build.VERSION_CODES.O)
    private fun saveDataIntoDatabase(imageString: String) {
        val username = profileViewModel.getUsername().value!!
        val text = inputView.text.toString()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formattedTime = current.format(formatter)

        val profilepicture = profileViewModel.getProfilepicture().value!!

        val myWrite = MomentsData("", username, text, imageString, formattedTime, profilepicture)
        momentsDataConnection.postMoment(myWrite)

    }*/

    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE) {
            val imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }
}