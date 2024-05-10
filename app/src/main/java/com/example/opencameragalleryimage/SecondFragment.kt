package com.example.opencameragalleryimage

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File


class SecondFragment : Fragment() {

    private lateinit var captureImg: ImageView
    private lateinit var captureBtn: Button
    private lateinit var galleryBtn: Button

    private lateinit var imageUri: Uri


    private val open_cam = registerForActivityResult(ActivityResultContracts.TakePicture()){
        captureImg.setImageURI(null)
        captureImg.setImageURI(imageUri)
    }

    private val open_gallery = registerForActivityResult(ActivityResultContracts.GetContent()){
        captureImg.setImageURI(it)
    }

     private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {permissions->

//            permissions is Map<String> so convert it boolean

//            first methond to convert permission into boolean

//            val allpermissionGranted = permissions.all { entry -> entry.value }


//            or use second method
            if (permissions[Manifest.permission.CAMERA]==true && permissions[Manifest.permission.ACCESS_COARSE_LOCATION]==true) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                open_cam.launch(imageUri)

            }
            else if (permissions[Manifest.permission.CAMERA]==false || permissions[Manifest.permission.ACCESS_COARSE_LOCATION]==false) {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.

                Toast.makeText(requireContext(), "Permission is denied", Toast.LENGTH_SHORT).show()
            }
        }



    fun checkMultiPermission() {
        if (
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)  {
                // You can use the API that requires the permission.
                open_cam.launch(imageUri)
            }

           else if( ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.CAMERA
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            )) {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                showAlertDialog()

            }

            else  {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.

                camePermission()

            }
        }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        captureImg = view.findViewById(R.id.imageView2)
        captureBtn = view.findViewById(R.id.capture_button2)
        galleryBtn = view.findViewById(R.id.gallery_button2)

        //create image uri using method created
        imageUri = createImageUri()



        galleryBtn.setOnClickListener {
            open_gallery.launch("image/*")
        }

        captureBtn.setOnClickListener {
            checkMultiPermission()

        }

    }

    // use require context whe using fragments
    // use context as application context when using activities

    private fun createImageUri(): Uri {
        //create camera image file
        val image = File(requireContext().filesDir,"cam_image.png")
        // find file uri(path)

        return FileProvider.getUriForFile(requireContext(),"com.example.opencameragalleryimage.fileProvider",image)
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Permission is required")
            .setMessage("If you are not going to give this permission you wont be able to use this feature.")
            .setPositiveButton("OK") { _, _ ->
                // Handle positive button click
                // You can perform actions or dismiss the dialog
                camePermission()

            }
            .setNegativeButton("Cancel") { dialog, which ->
                // Handle negative button click
                // You can perform actions or dismiss the dialog
                dialog.dismiss()
            }

            .show()
    }


    private fun camePermission(){
        // launch permission for camera
        requestPermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION) )
    }










}