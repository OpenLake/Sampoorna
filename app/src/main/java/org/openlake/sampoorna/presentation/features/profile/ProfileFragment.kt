package org.openlake.sampoorna.presentation.features.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.FragmentProfileBinding

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    var isEditing : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)

        val profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        profileViewModel.userDetails.observe(viewLifecycleOwner){ user->
            binding.userName.text = user.name
            binding.userAge.text = null
            binding.userEmail.text = user.email
            binding.userAddress.text = null
            binding.userBgrp.text = null
        }

        binding.profileEditFab.setOnClickListener {
            if(isEditing)
            {
                binding.userName.text = binding.userNameEdit.text
                profileViewModel.updateName(binding.userNameEdit.text.toString())

                if(binding.userAgeEdit.text.toString().isNotEmpty()){
                    binding.userAge.text = binding.userAgeEdit.text.toString() + getString(R.string.years)
                    profileViewModel.updateAge(Integer.parseInt(binding.userAgeEdit.text.toString()))
                }

                binding.userEmail.text = binding.userEmailEdit.text
                profileViewModel.updateEmail(binding.userEmailEdit.text.toString())

                binding.userAddress.text = binding.userAddressEdit.text
                profileViewModel.updateAddress(binding.userAddressEdit.text.toString())

                binding.userBgrp.text = binding.userBgrpEdit.text
                profileViewModel.updateBloodGroup(binding.userBgrpEdit.text.toString())

                Toast.makeText(requireContext(),"Changes saved",Toast.LENGTH_SHORT).show()

                closeEditingViews()
            }
            else
            {
                openEditingViews()
            }
        }

        binding.profileScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if(scrollY>oldScrollY) {
                binding.profileEditFab.shrink()
            } else {
                binding.profileEditFab.extend()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun closeEditingViews()
    {
        isEditing = false
        binding.profileEditFab.setIconResource(R.drawable.ic_edit)
        binding.profileEditFab.text = getString(R.string.edit)

        binding.userName.visibility = View.VISIBLE
        binding.userNameEditLayout.visibility = View.GONE

        binding.userAge.visibility = View.VISIBLE
        binding.userAgeEditLayout.visibility = View.GONE

        binding.userEmail.visibility = View.VISIBLE
        binding.userEmailEditLayout.visibility = View.GONE

        binding.userAddress.visibility = View.VISIBLE
        binding.userAddressEditLayout.visibility = View.GONE

        binding.userBgrp.visibility = View.VISIBLE
        binding.userBgrpEditLayout.visibility = View.GONE
    }

    private fun openEditingViews()
    {
        isEditing = true
        binding.profileEditFab.setIconResource(R.drawable.ic_done)
        binding.profileEditFab.text = getString(R.string.save)

        binding.userNameEdit.setText(binding.userName.text)
        binding.userName.visibility = View.GONE
        binding.userNameEditLayout.visibility = View.VISIBLE

        binding.userAgeEdit.setText(binding.userAge.text.toString().slice(0..(binding.userAge.text.toString().length-7)))
        binding.userAge.visibility = View.GONE
        binding.userAgeEditLayout.visibility = View.VISIBLE

        binding.userEmailEdit.setText(binding.userEmail.text)
        binding.userEmail.visibility = View.GONE
        binding.userEmailEditLayout.visibility = View.VISIBLE

        binding.userAddressEdit.setText(binding.userAddress.text)
        binding.userAddress.visibility = View.GONE
        binding.userAddressEditLayout.visibility = View.VISIBLE

        binding.userBgrpEdit.setText(binding.userBgrp.text)
        binding.userBgrp.visibility = View.GONE
        binding.userBgrpEditLayout.visibility = View.VISIBLE
    }

}