package edu.rosehulman.randomoutfitgenerator.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.randomoutfitgenerator.Constants
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.adapters.ClosetAdapter
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentClosetBinding
import edu.rosehulman.randomoutfitgenerator.models.Closet
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import kotlinx.coroutines.flow.DEFAULT_CONCURRENCY_PROPERTY_NAME

class ClosetFragment : Fragment() {
    private lateinit var binding: FragmentClosetBinding
    private lateinit var topsAdapter: ClosetAdapter
    private lateinit var bottomsAdapter: ClosetAdapter
    private lateinit var shoesAdapter: ClosetAdapter
    private lateinit var accessoriesAdapter: ClosetAdapter
    private lateinit var fullBodyAdapter: ClosetAdapter

    private lateinit var model: ClosetViewModel

    companion object{
        const val closetListener = "ClosetFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentClosetBinding.inflate(inflater, container, false)
        binding.closetParentLayout.visibility = View.INVISIBLE
        binding.loadingText.visibility = View.VISIBLE

        model = ViewModelProvider(requireActivity()).get(ClosetViewModel::class.java)
        setAdapters()
        model.addClothingListener(closetListener){
            topsAdapter.filter()
            topsAdapter.notifyDataSetChanged()
            bottomsAdapter.filter()
            bottomsAdapter.notifyDataSetChanged()
            accessoriesAdapter.filter()
            accessoriesAdapter.notifyDataSetChanged()
            shoesAdapter.filter()
            shoesAdapter.notifyDataSetChanged()
            fullBodyAdapter.filter()
            fullBodyAdapter.notifyDataSetChanged()
            binding.loadingText.visibility = View.GONE
            binding.closetParentLayout.visibility = View.VISIBLE
        }
        setListeners()

        return binding.root
    }

    /**
     * Sets the adapters for each of the five recycler views used in the fragment. Additionally it
     * sets the visibility of all the the view to View.GONE so that the user can choose which view
     * is visible
     */
    private fun setAdapters(){
        topsAdapter = ClosetAdapter(this, Closet.superCategories[0])
        bottomsAdapter = ClosetAdapter(this, Closet.superCategories[1])
        accessoriesAdapter = ClosetAdapter(this, Closet.superCategories[2])
        shoesAdapter = ClosetAdapter(this, Closet.superCategories[3])
        fullBodyAdapter = ClosetAdapter(this, Closet.superCategories[4])

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


        binding.topsGrid.visibility = View.GONE
        binding.bottomsGrid.visibility = View.GONE
        binding.accessoriesGrid.visibility = View.GONE
        binding.shoesGrid.visibility = View.GONE
        binding.fullBodyGrid.visibility = View.GONE

    }

    /**
     * Puts listeners on each of the text views to control the visibility of the corresponding
     * recycler view. It toggles the visibility of the selected view between View.VISIBLE and
     * View.GONE. It also sets the other four recyclers to Visibilty.GONE to recuse clutter on the
     * screen
     */
    fun setListeners(){
        binding.closetTops.setOnClickListener{
            if(binding.topsGrid.visibility == View.GONE){
                binding.topsGrid.visibility = View.VISIBLE
            }else{
                binding.topsGrid.visibility = View.GONE
            }

            binding.bottomsGrid.visibility = View.GONE
            binding.accessoriesGrid.visibility = View.GONE
            binding.shoesGrid.visibility = View.GONE
            binding.fullBodyGrid.visibility = View.GONE
        }

        binding.closetBottoms.setOnClickListener{
            binding.topsGrid.visibility = View.GONE

            if(binding.bottomsGrid.visibility == View.GONE){
                binding.bottomsGrid.visibility = View.VISIBLE
            }else{
                binding.bottomsGrid.visibility = View.GONE
            }

            binding.accessoriesGrid.visibility = View.GONE
            binding.shoesGrid.visibility = View.GONE
            binding.fullBodyGrid.visibility = View.GONE
        }

        binding.closetAccessories.setOnClickListener{
            binding.topsGrid.visibility = View.GONE
            binding.bottomsGrid.visibility = View.GONE

            if(binding.accessoriesGrid.visibility == View.GONE){
                binding.accessoriesGrid.visibility = View.VISIBLE
            }else{
                binding.accessoriesGrid.visibility = View.GONE
            }

            binding.shoesGrid.visibility = View.GONE
            binding.fullBodyGrid.visibility = View.GONE
        }

        binding.closetShoes.setOnClickListener{
            binding.topsGrid.visibility = View.GONE
            binding.bottomsGrid.visibility = View.GONE
            binding.accessoriesGrid.visibility = View.GONE

            if(binding.shoesGrid.visibility == View.GONE){
                binding.shoesGrid.visibility = View.VISIBLE
            }else{
                binding.shoesGrid.visibility = View.GONE
            }

            binding.fullBodyGrid.visibility = View.GONE
        }

        binding.closetFullBody.setOnClickListener {
            binding.topsGrid.visibility = View.GONE
            binding.bottomsGrid.visibility = View.GONE
            binding.accessoriesGrid.visibility = View.GONE
            binding.shoesGrid.visibility = View.GONE

            if(binding.fullBodyGrid.visibility == View.GONE){
                binding.fullBodyGrid.visibility = View.VISIBLE
            }else{
                binding.fullBodyGrid.visibility = View.GONE
            }
        }

        binding.cameraFab.setOnClickListener {
            if(requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                findNavController().navigate(R.id.nav_camera)
            }
        }
    }

    override fun onDestroyView(){
        super.onDestroyView()
        model.removeListener(closetListener)
    }
}