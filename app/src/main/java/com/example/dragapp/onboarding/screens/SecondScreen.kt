package com.example.dragapp.onboarding.screens

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.dragapp.R
import com.example.dragapp.databinding.FragmentLoginBinding
import com.example.dragapp.databinding.FragmentSecondScreenBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_second_screen.view.*


class SecondScreen : Fragment() {

    companion object{
        private const val CAMERA = 1
        private const val GALLERY = 2
    }

    private var _binding: FragmentSecondScreenBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        // data binding
        _binding = FragmentSecondScreenBinding.inflate(inflater, container, false)
        // set so ViewModel livedata can be observed from layout
        //binding.lifecycleOwner = this // works only with databinding layout

        binding.cameraBt.setOnClickListener {
            // Toast.makeText(requireContext(), "Camera clicked", Toast.LENGTH_SHORT).show()
            Dexter.withContext(requireContext()).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                // Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let{
                        if(report.areAllPermissionsGranted()){
                            val intent =  Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, CAMERA)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationaleDialogForPermissions()
                }

            }).onSameThread().check()
        }

        binding.galleryBt.setOnClickListener {
            // Toast.makeText(requireContext(), "Gallery clicked", Toast.LENGTH_SHORT).show()
            Dexter.withContext(requireContext()).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ).withListener(object: PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val galleryIntent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(requireContext(), "You have denied Gallery permission, to select an image!", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    showRationaleDialogForPermissions()
                }


            }).onSameThread().check()
        }

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.nextButtonSs.setOnClickListener {
            viewPager?.currentItem = 2
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA){
                data?.extras?.let{
                    val thumbnail : Bitmap = data.extras!!.get("data") as Bitmap
                    // binding.profileImage.setImageBitmap(thumbnail)
                    Glide.with(this)
                        .load(thumbnail)
                        .circleCrop()
                        .into(binding.profileImage)
                }
            }
            if(requestCode == GALLERY){
                data?.let{
                    val imageUri = data.data
                    // binding.profileImage.setImageURI(imageUri)
                    Glide.with(this)
                        .load(imageUri)
                        .circleCrop()
                        .into(binding.profileImage)
                }
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("CANCELED:", "canceled image selection")
        }
    }

    private fun showRationaleDialogForPermissions() {
        AlertDialog.Builder(requireContext()).setMessage("Truned of Permissions")
            .setPositiveButton("Go to settings"){
                _,_ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireContext().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){dialog,_ -> dialog.dismiss()}
            .show()

    }

}