package com.example.dragapp.onboarding.screens

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.dragapp.DashboardActivity
import com.example.dragapp.R
import com.example.dragapp.api.RetrofitInterceptor
import com.example.dragapp.databinding.FragmentLoginBinding
import com.example.dragapp.databinding.FragmentSecondScreenBinding
import com.example.dragapp.repositories.DragRepository
import com.example.dragapp.viewmodels.DragViewModel
import com.example.dragapp.viewmodels.DragViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_second_screen.view.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class SecondScreen : Fragment() {

    private lateinit var mDragViewModel: DragViewModel
    private var _binding: FragmentSecondScreenBinding? = null
    private val binding get() = _binding!!
    private var mImagePath : String = ""

    companion object{
        private const val CAMERA = 1
        private const val GALLERY = 2

        private const val IMAGE_DIRECTORY = "DragAppImages"
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        // data binding
        _binding = FragmentSecondScreenBinding.inflate(inflater, container, false)
        // set so ViewModel livedata can be observed from layout
        //binding.lifecycleOwner = this // works only with databinding layout

        // DragViewModel init
        val dragRepository = DragRepository()
        val dragViewModelFactory = DragViewModelFactory(dragRepository)
        mDragViewModel = ViewModelProvider(this, dragViewModelFactory).get(DragViewModel::class.java)


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

                    mImagePath = saveImageToInternalStorage(thumbnail)
                    Log.i("Profile Image PATH:", mImagePath)

                    uploadImage()
                    mDragViewModel.currentProfileImageData.observe(viewLifecycleOwner, { response ->
                        if(response.isSuccessful){
                            Log.d("Body:", response.body().toString())
                            Log.d("Headers:", response.headers().toString())

                            val imageString = response.body()?.data.toString()


                            Log.d("Image String:", imageString)


                        }else{
                            Toast.makeText(requireContext(), "error image upload", Toast.LENGTH_SHORT).show()

                        }
                    })
                }
            }
            if(requestCode == GALLERY){
                data?.let{
                    val imageUri = data.data
                    // binding.profileImage.setImageURI(imageUri)
                    Glide.with(this)
                        .load(imageUri)
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object: RequestListener<Drawable>{
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e("ERROR IMAGE:", "error loading image")
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let{
                                    val bitmap: Bitmap = resource.toBitmap()
                                    mImagePath = saveImageToInternalStorage(bitmap)

                                }
                                return false
                            }

                        })
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

    private fun saveImageToInternalStorage(bitmap: Bitmap):String{
        val wrapper = ContextWrapper(requireContext().applicationContext)

        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)

        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream : OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }

        return file.absolutePath
    }

    private fun uploadImage(){
        val file = File(mImagePath)

        val requestBody = RequestBody.create("image/*".toMediaType(), file)
        val parts = MultipartBody.Part.createFormData("file", file.name, requestBody)

        mDragViewModel.uploadProfileImage(parts)

    }

}