package com.example.opencameragalleryimage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class FirstFragment : Fragment() {


    companion object {

        private const val CAMERA_PERMISSION_CODE = 11
        private const val CAMERA_INTENT_CODE = 22

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view is required to find id by findViewById

        val capture_btn1 = requireView().findViewById<Button>(R.id.capture_button1)
        val next_btn = requireView().findViewById<Button>(R.id.nextbtn)


        //go to second frag using action.
        //You can also use directions.action
        //second frag captures image using registeractivityforesut

        next_btn.setOnClickListener {
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }


        //capture image using camera with StarActivityForResult

        capture_btn1.setOnClickListener {

            //check permission for using the camera
            if ((ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(camIntent, CAMERA_INTENT_CODE)
            }else{
                showAlertDialog()

            }
        }




    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(camIntent, CAMERA_INTENT_CODE)
            }else{
                Toast.makeText(requireContext(), "You denied permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA_INTENT_CODE){
                val getImage: Bitmap = data!!.extras!!.get("data") as Bitmap

                val image1 = requireView().findViewById<ImageView>(R.id.imageView1)
                image1!!.setImageBitmap(getImage)
            }
        }
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Permission is required")
            .setMessage("If you are not going to give this permission you wont be able to use this feature.")
            .setPositiveButton("OK") { _, _ ->
                // Handle positive button click
                // You can perform actions or dismiss the dialog
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION), CAMERA_PERMISSION_CODE)
            }
            .setNegativeButton("Cancel") { dialog, which ->
                // Handle negative button click
                // You can perform actions or dismiss the dialog
                dialog.dismiss()
            }

            .show()
    }




}