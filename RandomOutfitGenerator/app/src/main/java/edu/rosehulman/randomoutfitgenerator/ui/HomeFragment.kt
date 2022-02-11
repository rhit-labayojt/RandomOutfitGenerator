package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Carousel
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.adapters.CarouselAdapter
import edu.rosehulman.randomoutfitgenerator.adapters.OutfitAccessoriesAdapter
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentHomeBinding
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var model: ClosetViewModel

    companion object{
        const val fragmentName = "HomeFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(ClosetViewModel::class.java)

        binding.randomOutfitFab.visibility = View.GONE
        binding.weatherWidget.visibility = View.GONE
        binding.outfitCarousel.visibility = View.GONE

        model.addRecentOutfitsListener(fragmentName){
            binding.randomOutfitFab.visibility = View.VISIBLE
            binding.weatherWidget.visibility = View.VISIBLE
            binding.outfitCarousel.visibility = View.VISIBLE

            binding.outfitCarousel.adapter = CarouselAdapter(this)
        }

        binding.outfitCarousel.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.outfitCarousel.setHasFixedSize(true)
        binding.outfitCarousel.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))

        binding.randomOutfitFab.setOnClickListener {
            findNavController().navigate(R.id.nav_randomization)
        }

        return binding.root
    }

    override fun onDestroyView(){
        super.onDestroyView()
        model.removeListener(fragmentName)
    }


}