package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.adapters.OutfitDisplayAdapter
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
        binding.outfitCarousel.visibility = View.GONE
        binding.emptyCarouselImage.visibility = View.GONE
        binding.recentOutfitsLabel.visibility = View.GONE

        model.addRecentOutfitsListener(fragmentName){
            binding.randomOutfitFab.visibility = View.VISIBLE
            binding.recentOutfitsLabel.visibility = View.VISIBLE

            binding.outfitCarousel.adapter = OutfitDisplayAdapter(this, model.closet.recentOutfits)

            if(model.closet.recentOutfits.isEmpty()){
                binding.outfitCarousel.visibility = View.GONE
                binding.emptyCarouselImage.visibility = View.VISIBLE

                binding.outfitHolder.setCardBackgroundColor(requireActivity().getColor(R.color.white))
            }else{
                binding.emptyCarouselImage.visibility = View.GONE
                binding.outfitCarousel.visibility = View.VISIBLE

                binding.outfitCarousel.apply{
                    layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    setHasFixedSize(true)
                    var itemDecoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
                    itemDecoration.setDrawable(getDrawable(requireContext(), R.drawable.thick_divider_vertical)!!)
                    addItemDecoration(itemDecoration)
                }
            }
        }

        binding.randomOutfitFab.setOnClickListener {
            findNavController().navigate(R.id.nav_randomization)
        }
        return binding.root
    }


}