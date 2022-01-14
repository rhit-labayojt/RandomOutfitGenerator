package edu.rosehulman.randomoutfitgenerator.ui

import android.graphics.Camera
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentCameraBinding
import edu.rosehulman.randomoutfitgenerator.objects.CameraPreview

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
//    private lateinit var cam: Camera?
//    private lateinit var preview: CameraPreview?

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        binding = FragmentCameraBinding.inflate(inflater, container, false)
//        cam = getCameraInstance()

        return binding.root
    }

//    fun getCameraInstance(): Camera? {
//        return try {
//            Camera.open()
//        } catch (e: Exception) {
//            null
//        }
//    }

}