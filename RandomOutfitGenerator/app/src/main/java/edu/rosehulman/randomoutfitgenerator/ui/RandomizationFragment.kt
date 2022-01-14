package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentRandomizationBinding

class RandomizationFragment: Fragment() {
    private lateinit var binding: FragmentRandomizationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRandomizationBinding.inflate(inflater, container, false)

        return binding.root
    }
}