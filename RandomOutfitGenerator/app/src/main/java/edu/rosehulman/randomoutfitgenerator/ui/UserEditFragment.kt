package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.adapters.TagsAdapter
import edu.rosehulman.randomoutfitgenerator.adapters.TagsHolderAdapter
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentUserEditBinding
import edu.rosehulman.randomoutfitgenerator.models.UserViewModel

class UserEditFragment: Fragment() {
    private lateinit var binding: FragmentUserEditBinding
    private lateinit var userModel: UserViewModel
    private var tagTypes = arrayOf("Styles", "Tops", "Bottoms", "Shoes", "Full Body", "Accessories")
    private lateinit var editableTagHolderAdapter: TagsHolderAdapter
    private lateinit var viewableTagHolderAdapter: TagsHolderAdapter
    private var editableTagsAdapters = arrayListOf<TagsAdapter>()
    private var viewableTagsAdapters = arrayListOf<TagsAdapter>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserEditBinding.inflate(inflater, container, false)
        userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        binding.userEditChangeName.setText(binding.userEditUserName.text)

        if(userModel.editUser){
            binding.userEditUserName.visibility = View.GONE
            binding.userEditChangeName.visibility = View.VISIBLE
        }else{
            binding.userEditChangeName.visibility = View.GONE
            binding.userEditUserName.visibility = View.VISIBLE
        }

        tagTypes.forEach{ userModel.buildChangesMap(it) }
        userModel.buildChangesMap("defaultStyle")

        if(Firebase.auth.currentUser!!.email != null){
            binding.userEditLoginMethod.setText("Email: ${Firebase.auth.currentUser!!.email}")
        }else if(Firebase.auth.currentUser!!.phoneNumber != null){
            binding.userEditLoginMethod.setText("Email: ${Firebase.auth.currentUser!!.phoneNumber}")
        }

        makeAdapters()
        setup()

        return binding.root
    }

    private fun makeAdapters(){
        tagTypes.forEach{
            when(it){
                "Tops" -> {
                    editableTagsAdapters.add(TagsAdapter(this, R.layout.checkable_tag_view, it, userModel.user!!.topsTags))
                    viewableTagsAdapters.add(TagsAdapter(this, R.layout.tag_view, it, userModel.user!!.topsTags))
                }
                "Bottoms" -> {
                    editableTagsAdapters.add(TagsAdapter(this, R.layout.checkable_tag_view, it, userModel.user!!.bottomsTags))
                    viewableTagsAdapters.add(TagsAdapter(this, R.layout.tag_view, it, userModel.user!!.bottomsTags))
                }
                "Shoes" -> {
                    editableTagsAdapters.add(TagsAdapter(this, R.layout.checkable_tag_view, it, userModel.user!!.shoesTags))
                    viewableTagsAdapters.add(TagsAdapter(this, R.layout.tag_view, it, userModel.user!!.shoesTags))
                }
                "Styles" -> {
                    editableTagsAdapters.add(TagsAdapter(this, R.layout.checkable_tag_view, it, userModel.user!!.styles))
                    viewableTagsAdapters.add(TagsAdapter(this, R.layout.tag_view, it, userModel.user!!.styles))
                }
                "Accessories" -> {
                    editableTagsAdapters.add(TagsAdapter(this, R.layout.checkable_tag_view, it, userModel.user!!.accessoriesTags))
                    viewableTagsAdapters.add(TagsAdapter(this, R.layout.tag_view, it, userModel.user!!.accessoriesTags))
                }
                else -> {
                    editableTagsAdapters.add(TagsAdapter(this, R.layout.checkable_tag_view, it, userModel.user!!.fullBodyTags))
                    viewableTagsAdapters.add(TagsAdapter(this, R.layout.tag_view, it, userModel.user!!.fullBodyTags))
                }
            }
        }

        editableTagHolderAdapter = TagsHolderAdapter(this, tagTypes, editableTagsAdapters)
        viewableTagHolderAdapter = TagsHolderAdapter(this, tagTypes, viewableTagsAdapters)
    }

    private fun setup(){
        binding.editButton.setOnClickListener {
            if(userModel.editUser){
                userModel.update(binding.userEditChangeName.text.toString(), true)
            }

            userModel.editUser = !userModel.editUser
            updateView()
        }

        binding.logoutButton.setOnClickListener {
            findNavController().navigate(R.id.nav_splash)
            Firebase.auth.signOut()
            userModel.user = null
        }

        if(userModel.editUser){
            binding.userEditTags.adapter = editableTagHolderAdapter
        }else{
            binding.userEditTags.adapter = viewableTagHolderAdapter
        }

        binding.userEditTags.layoutManager = LinearLayoutManager(requireContext())
        binding.userEditTags.setHasFixedSize(true)
        binding.userEditTags.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun updateView(){
        if(userModel.editUser){
            binding.userEditUserName.visibility = View.GONE
            binding.logoutButton.visibility = View.INVISIBLE
            binding.userEditChangeName.visibility = View.VISIBLE

            binding.userEditChangeName.setText(userModel.user!!.name)
            binding.editButton.setText("Done")
            binding.userEditTags.adapter = viewableTagHolderAdapter
        }else{
            binding.userEditChangeName.visibility = View.GONE
            binding.logoutButton.visibility = View.VISIBLE
            binding.userEditUserName.visibility = View.VISIBLE

            binding.userEditUserName.setText(userModel.user!!.name)
            binding.editButton.setText("Edit")
            binding.userEditTags.adapter = editableTagHolderAdapter
        }
    }

}