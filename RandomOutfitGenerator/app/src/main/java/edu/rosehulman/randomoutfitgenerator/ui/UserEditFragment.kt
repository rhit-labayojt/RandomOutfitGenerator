package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentUserEditBinding
import edu.rosehulman.randomoutfitgenerator.models.UserViewModel

class UserEditFragment: Fragment() {
    private lateinit var binding: FragmentUserEditBinding
    private lateinit var userModel: UserViewModel

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

        return binding.root
    }

}