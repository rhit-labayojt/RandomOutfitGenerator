package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentClothingEditBinding

class ClothingEditFragment: Fragment() {
    private lateinit var binding: FragmentClothingEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentClothingEditBinding.inflate(inflater, container, false)

        return binding.root
    }
}