package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.adapters.OutfitAccessoriesAdapter
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentOutfitBinding
import edu.rosehulman.randomoutfitgenerator.models.Closet
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel

class OutfitFragment: Fragment() {
    private lateinit var binding: FragmentOutfitBinding
    private lateinit var model: ClosetViewModel
    private var images = mutableMapOf<ImageView, String>()

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

        images.put(binding.outfitTop, model.currentOutfit!!.top)
        images.put(binding.outfitBottom, model.currentOutfit!!.bottom)
        images.put(binding.outfitShoes, model.currentOutfit!!.shoes)

        setupImages()
        setupText()

        return binding.root
    }

    fun deleteOutfit(){
        model.deleteCurrentOutfit()
    }

    private fun setupImages(){
        images.keys.forEach {
            it.load(images[it]){
                crossfade(true)
                transformations(RoundedCornersTransformation())
            }
        }

        val accessoryAdapter = OutfitAccessoriesAdapter(this)
        binding.outfitAccessories.adapter = accessoryAdapter
        binding.outfitAccessories.layoutManager = LinearLayoutManager(requireContext())
        binding.outfitAccessories.setHasFixedSize(true)
        binding.outfitAccessories.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

    }

    private fun setupText(){
        binding.outfitTopCat.text = "Top: ${model.currentOutfit!!.topCat}"
        binding.outfitBottomCat.text = "Bottom: ${model.currentOutfit!!.bottomCat}"
        binding.outfitShoeCat.text = "Shoes: ${model.currentOutfit!!.shoesCat}"
        binding.outfitAccessoriesCat.text = "Accessories: ${model.closet.toString(model.currentOutfit!!.accessoriesCats)}"
        binding.outfitWeather.text = "Weather: ${model.currentOutfit!!.weather}"
        binding.outfitStyle.text = "Style: ${model.currentOutfit!!.style}"
    }
}