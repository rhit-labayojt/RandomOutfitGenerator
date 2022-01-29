package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentOutfitBinding
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel

class OutfitFragment: Fragment() {
    private lateinit var binding: FragmentOutfitBinding
    private lateinit var model: ClosetViewModel
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        inflater.inflate(R.menu.menu_outfit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        return when(item.itemId){
            R.id.delet_outfit -> {
                deleteOutfit()
                findNavController().navigate(R.id.nav_saved_outfits)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOutfitBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(ClosetViewModel::class.java)

        return binding.root
    }

    fun deleteOutfit(){
        model.deleteCurrentOutfit()
    }
}