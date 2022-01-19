package edu.rosehulman.randomoutfitgenerator.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.adapters.ClosetAdapter
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentClosetBinding
import edu.rosehulman.randomoutfitgenerator.objects.SuperCategory

class ClosetFragment : Fragment() {
    private lateinit var binding: FragmentClosetBinding
    lateinit var adapterList: Array<RecyclerView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentClosetBinding.inflate(inflater, container, false)

        val topsAdapter = ClosetAdapter(this, SuperCategory.Top)
        val bottomsAdapter = ClosetAdapter(this, SuperCategory.Bottom)
        val shoesAdapter = ClosetAdapter(this,SuperCategory.Shoes)
        val fullBodyAdapter = ClosetAdapter(this, SuperCategory.FullBody)
        val accessoriesAdapter = ClosetAdapter(this, SuperCategory.Accessory)

        binding.topsGrid.adapter = topsAdapter
        binding.bottomsGrid.adapter = bottomsAdapter
        binding.shoesGrid.adapter = shoesAdapter
        binding.accessoriesGrid.adapter = accessoriesAdapter
        binding.fullBodyGrid.adapter = fullBodyAdapter

        binding.topsGrid.layoutManager = GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
        binding.bottomsGrid.layoutManager = GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
        binding.shoesGrid.layoutManager = GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
        binding.accessoriesGrid.layoutManager = GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
        binding.fullBodyGrid.layoutManager = GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)

        adapterList = Array(5){binding.topsGrid; binding.bottomsGrid; binding.shoesGrid; binding.accessoriesGrid; binding.fullBodyGrid}

        binding.closetTops.setOnClickListener{
            setViewVisible(binding.topsGrid)
        }

        binding.closetBottoms.setOnClickListener{
            setViewVisible(binding.bottomsGrid)
        }

        binding.closetAccessories.setOnClickListener{
            setViewVisible(binding.accessoriesGrid)
        }

        binding.closetShoes.setOnClickListener{
            setViewVisible(binding.shoesGrid)
        }

        binding.closetFullBody.setOnClickListener {
            setViewVisible(binding.fullBodyGrid)
        }

        binding.cameraFab.setOnClickListener {
            if(requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                findNavController().navigate(R.id.nav_camera)
            }
        }

        setViewVisible(binding.topsGrid)

        return binding.root
    }

    fun setViewVisible(view: RecyclerView){
        for(i in adapterList){
            if(i == view ){
                view.visibility = View.VISIBLE
            }else{
                view.visibility = View.GONE
            }
        }
    }


}