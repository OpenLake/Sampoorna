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
        profileViewModel.getUser()

        profileViewModel.user.observe(viewLifecycleOwner){ user->
            binding.userName.text = user.name
            user.age?.let {
                binding.userAge.text = it.toString()
            }
            binding.userEmail.text = user.email
            binding.userAbout.text = user.about
            binding.userUsername.text = user.username
        }

        binding.profileEditFab.setOnClickListener {
            if(isEditing)
            {
                binding.userName.text = binding.userNameEdit.text

                if(binding.userAgeEdit.text.toString().isNotEmpty()){
                    binding.userAge.text = binding.userAgeEdit.text.toString() + getString(R.string.years)
                }

                binding.userEmail.text = binding.userEmailEdit.text

                binding.userAbout.text = binding.userAboutEdit.text

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

        binding.userAbout.visibility = View.VISIBLE
        binding.userAboutEditLayout.visibility = View.GONE

        binding.userUsername.visibility = View.VISIBLE
    }

    private fun openEditingViews()
    {
        isEditing = true
        binding.profileEditFab.setIconResource(R.drawable.ic_done)
        binding.profileEditFab.text = getString(R.string.save)

        binding.userNameEdit.setText(binding.userName.text)
        binding.userName.visibility = View.GONE
        binding.userNameEditLayout.visibility = View.VISIBLE

        val age = binding.userAge.text.toString().slice(0..(binding.userAge.text.toString().length-7))
        binding.userAgeEdit.setText(when(age){
            "?" -> ""
            else -> age
        })
        binding.userAge.visibility = View.GONE
        binding.userAgeEditLayout.visibility = View.VISIBLE

        binding.userEmailEdit.setText(binding.userEmail.text)
        binding.userEmail.visibility = View.GONE
        binding.userEmailEditLayout.visibility = View.VISIBLE

        binding.userAboutEdit.setText(binding.userAbout.text)
        binding.userAbout.visibility = View.GONE
        binding.userAboutEditLayout.visibility = View.VISIBLE

        binding.userUsername.visibility = View.GONE
    }

}