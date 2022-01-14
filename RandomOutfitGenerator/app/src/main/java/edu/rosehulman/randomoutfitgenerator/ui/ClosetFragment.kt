package edu.rosehulman.randomoutfitgenerator.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentClosetBinding

class ClosetFragment : Fragment() {
    private lateinit var binding: FragmentClosetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentClosetBinding.inflate(inflater, container, false)

        binding.cameraFab.setOnClickListener {
            if(requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                findNavController().navigate(R.id.nav_camera)
            }
        }

        return binding.root
    }


}