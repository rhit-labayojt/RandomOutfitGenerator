package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.adapters.SavedOutfitsAdapter
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentSavedOutfitsBinding
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import edu.rosehulman.randomoutfitgenerator.models.UserViewModel

class SavedOutfitsFragment: Fragment() {
    private lateinit var binding: FragmentSavedOutfitsBinding
    private lateinit var model: ClosetViewModel
    private lateinit var userModel: UserViewModel

    companion object{
        const val fragmentName = "SavedOutfits"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSavedOutfitsBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(ClosetViewModel::class.java)
        userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        binding.savedOutfits.visibility = View.GONE
        model.addSavedOutfitsListener(fragmentName){
            binding.savedOutfits.visibility = View.VISIBLE
        }

        var styles = ArrayList<String>()
        styles.addAll(userModel.user!!.styles)
        styles.add("Other")

        binding.savedOutfits.adapter = SavedOutfitsAdapter(this, styles)
        binding.savedOutfits.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.savedOutfits.setHasFixedSize(true)

        return binding.root
    }
}