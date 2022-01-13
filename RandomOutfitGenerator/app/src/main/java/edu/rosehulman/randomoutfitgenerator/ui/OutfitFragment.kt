package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentOutfitBinding

class OutfitFragment: Fragment() {
    private lateinit var binding: FragmentOutfitBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOutfitBinding.inflate(inflater, container, false)

        return binding.root
    }
}