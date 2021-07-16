package com.example.dragapp.onboarding.screens

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.dragapp.R
import com.example.dragapp.databinding.FragmentSecondScreenBinding
import com.example.dragapp.services.AskPermissions
import com.example.dragapp.utils.Constants
import com.google.android.material.snackbar.Snackbar

class SecondScreen : Fragment() {
    private var _binding: FragmentSecondScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSecondScreenBinding.inflate(inflater, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        val askPermission = AskPermissions(requireContext(), requireActivity())
        askPermission.setupGalleryPermissions()

        binding.pictureButton.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(activity?.packageManager!!) != null) {
                startActivityForResult(takePictureIntent, Constants.REQUEST_CODE)
            } else {
                Snackbar.make(it, "Unable to open camera", Snackbar.LENGTH_SHORT).show()
            }

        }
        binding.galleryButton.setOnClickListener {
            selectImageInAlbum()
        }

        binding.nextButtonSs.setOnClickListener {
            viewPager?.currentItem = 2
        }
        return binding.root
    }

    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE) {
            val imageUri = data?.data
            binding.pictureIv.setImageURI(imageUri)
        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE) {
            val takenImage = data?.extras?.get("data") as Bitmap
            binding.pictureIv.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}