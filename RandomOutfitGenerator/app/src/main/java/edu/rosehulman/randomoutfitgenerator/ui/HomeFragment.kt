package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.adapters.OutfitDisplayAdapter
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentHomeBinding
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import edu.rosehulman.randomoutfitgenerator.models.UserViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var model: ClosetViewModel
    private lateinit var userModel: UserViewModel

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
        userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        var username = requireActivity().findViewById<TextView>(R.id.nav_drawer_name)
        var login = requireActivity().findViewById<TextView>(R.id.nav_drawer_login_info)
        username.setText("${userModel.user!!.name}")
        login.setText("Email: ${Firebase.auth.currentUser!!.email}")

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

    override fun onDestroyView() {
        super.onDestroyView()
        model.removeListener(fragmentName)
    }


}