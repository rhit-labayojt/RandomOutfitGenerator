package edu.rosehulman.randomoutfitgenerator.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.randomoutfitgenerator.Constants
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.adapters.TagsAdapter
import edu.rosehulman.randomoutfitgenerator.adapters.TagsHolderAdapter
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentUserEditBinding
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import edu.rosehulman.randomoutfitgenerator.models.UserViewModel

class UserEditFragment: Fragment() {
    private lateinit var binding: FragmentUserEditBinding
    private lateinit var userModel: UserViewModel
    private lateinit var model: ClosetViewModel
    private var tagTypes = arrayOf("Styles", "Tops", "Bottoms", "Shoes", "Full Body", "Accessories")
    private lateinit var tagHolderAdapter: TagsHolderAdapter
    private var tagsAdapters = arrayListOf<TagsAdapter>()

    companion object{
        const val savedOutfitsListener = "SavedOutfitsUserEdit"
        const val recentOutfitsListener = "RecentOutfitsUserEdit"
        const val clothingListener = "ClothingUserEdit"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.nav_settings)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserEditBinding.inflate(inflater, container, false)
        userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        model = ViewModelProvider(requireActivity()).get(ClosetViewModel::class.java)

        binding.userEditPage.visibility = View.GONE // Hide page until all clothing has been loaded

        setHasOptionsMenu(true)

        model.addRecentOutfitsListener(recentOutfitsListener){}
        model.addSavedOutfitsListener(savedOutfitsListener){}
        model.addClothingListener(clothingListener){
            binding.userEditPage.visibility = View.VISIBLE // Show the user_edit page after all clothing has been loaded

            binding.userEditUserName.setText(userModel.user!!.name)
            binding.userEditChangeName.setText(binding.userEditUserName.text)
            binding.userEditChangeName.setHint("Enter a name")

            if(userModel.editUser || !userModel.user!!.hasCompletedSetup){
                if(!userModel.editUser){
                    userModel.editUser = true
                }
                binding.userEditUserName.visibility = View.INVISIBLE
                binding.logoutButton.visibility = View.INVISIBLE
                binding.logoutButton.isClickable = false
                binding.userEditChangeName.visibility = View.VISIBLE

                binding.userEditChangeName.setText(userModel.user!!.name)
                binding.editButton.setText("Done")
            }else{
                binding.userEditChangeName.visibility = View.GONE
                binding.logoutButton.visibility = View.VISIBLE
                binding.logoutButton.isClickable = true
                binding.userEditUserName.visibility = View.VISIBLE

                binding.userEditUserName.setText(userModel.user!!.name)
                binding.editButton.setText("Edit")
            }

            tagTypes.forEach{ userModel.buildChangesMap(it) }
            userModel.buildChangesMap("defaultStyle")

            if(Firebase.auth.currentUser!!.email != null){
                binding.userEditLoginMethod.setText("Email: ${Firebase.auth.currentUser!!.email}")
            }else if(Firebase.auth.currentUser!!.phoneNumber != null){
                binding.userEditLoginMethod.setText("Phone Number: ${Firebase.auth.currentUser!!.phoneNumber}")
            }

            makeAdapters()
            setup()
            Log.d(Constants.TAG, "Setup Done")
        }

        return binding.root
    }

    private fun makeAdapters(){
        tagTypes.forEach{
            when(it){
                "Styles" -> {
                    tagsAdapters.add(TagsAdapter(this, it, userModel.user!!.styles))
                }
                "Tops" -> {
                    tagsAdapters.add(TagsAdapter(this, it, userModel.user!!.topsTags))
                }
                "Bottoms" -> {
                    tagsAdapters.add(TagsAdapter(this, it, userModel.user!!.bottomsTags))
                }
                "Shoes" -> {
                    tagsAdapters.add(TagsAdapter(this, it, userModel.user!!.shoesTags))
                }
                "Full Body" -> {
                    tagsAdapters.add(TagsAdapter(this, it, userModel.user!!.fullBodyTags))
                }
                else -> {
                    tagsAdapters.add(TagsAdapter(this, it, userModel.user!!.accessoriesTags))
                }
            }
        }

        tagHolderAdapter = TagsHolderAdapter(this, tagTypes, tagsAdapters)
    }

    private fun setup(){
        binding.editButton.setOnClickListener {
            if(userModel.editUser){
                userModel.update(binding.userEditChangeName.text.toString(), true)

                var username = requireActivity().findViewById<TextView>(R.id.nav_drawer_name)
                var login = requireActivity().findViewById<TextView>(R.id.nav_drawer_login_info)
                username.setText(userModel.user!!.name)
                login.setText("Email: ${Firebase.auth.currentUser!!.email}")
            }

            userModel.editUser = !userModel.editUser
            updateView()
        }

        binding.logoutButton.setOnClickListener {
            findNavController().navigate(R.id.nav_splash)
            Firebase.auth.signOut()
            userModel.user = null
        }

        binding.userEditTags.adapter = tagHolderAdapter
        binding.userEditTags.layoutManager = LinearLayoutManager(requireContext())
        binding.userEditTags.setHasFixedSize(true)
        binding.userEditTags.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun updateView(){
        if(userModel.editUser){
            Log.d(Constants.TAG,"Edit Button Pressed")
            binding.userEditUserName.visibility = View.INVISIBLE
            binding.logoutButton.visibility = View.INVISIBLE
            binding.logoutButton.isClickable = false
            binding.userEditChangeName.visibility = View.VISIBLE

            binding.userEditChangeName.setText(userModel.user!!.name)
            binding.editButton.setText("Done")
        }else{
            Log.d(Constants.TAG,"Done Button Pressed")
            binding.userEditChangeName.visibility = View.GONE
            binding.logoutButton.visibility = View.VISIBLE
            binding.logoutButton.isClickable = true
            binding.userEditUserName.visibility = View.VISIBLE

            binding.userEditUserName.setText(userModel.user!!.name)
            binding.editButton.setText("Edit")
        }

        (binding.userEditTags.adapter as TagsHolderAdapter).update()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        model.removeListener(recentOutfitsListener)
        model.removeListener(savedOutfitsListener)
        model.removeListener(clothingListener)
    }

}