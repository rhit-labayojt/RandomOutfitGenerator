package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.adapters.SavedOutfitsAdapter
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentSavedOutfitsBinding
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import edu.rosehulman.randomoutfitgenerator.models.UserViewModel
import edu.rosehulman.randomoutfitgenerator.objects.SavedOutfitStyles

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
        binding.noSavedOutfits.visibility = View.GONE

        model.addSavedOutfitsListener(fragmentName){
            if(model.closet.savedOutfits.isEmpty()){
                binding.savedOutfits.visibility = View.GONE
                binding.noSavedOutfits.visibility =View.VISIBLE
            }else {
                binding.noSavedOutfits.visibility = View.GONE
                binding.savedOutfits.visibility = View.VISIBLE

                var styles = ArrayList<SavedOutfitStyles>()
                userModel.user!!.styles.forEach { styles.add(SavedOutfitStyles(it, false)) }
                styles.add(SavedOutfitStyles("Other", false))

                binding.savedOutfits.adapter = SavedOutfitsAdapter(this, styles)
                binding.savedOutfits.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.savedOutfits.setHasFixedSize(true)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        model.removeListener(fragmentName)
    }
}