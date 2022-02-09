package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentSavedOutfitsBinding
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel

class SavedOutfitsFragment: Fragment() {
    private lateinit var binding: FragmentSavedOutfitsBinding
    private lateinit var model: ClosetViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSavedOutfitsBinding.inflate(inflater, container, false)

        return binding.root
    }
}