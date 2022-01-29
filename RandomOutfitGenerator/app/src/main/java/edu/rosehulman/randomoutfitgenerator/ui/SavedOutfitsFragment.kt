package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentSavedOutfitsBinding

class SavedOutfitsFragment: Fragment() {
    private lateinit var binding: FragmentSavedOutfitsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSavedOutfitsBinding.inflate(inflater, container, false)

        return binding.root
    }
}