package org.openlake.sampoorna.presentation.features.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.App
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.databinding.FragmentProfileBinding

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    var isEditing : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)

        sharedPreferences = requireActivity().getSharedPreferences(Constants.Sampoorna, Context.MODE_PRIVATE)

        val profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        profileViewModel.getUser()

        binding.userName.text = sharedPreferences.getString(Constants.Name, "")
        binding.userUsername.text = sharedPreferences.getString(Constants.Username, "")
        sharedPreferences.getInt(Constants.Age, -1).let {
            if(it != -1) {
                binding.userAge.text = "$it years"
            }
        }
        binding.userEmail.text = sharedPreferences.getString(Constants.Email, "")
        binding.userAbout.text = sharedPreferences.getString(Constants.About, "")

        profileViewModel.user.observe(viewLifecycleOwner){ user->
            binding.userName.text = user.name
            user.age?.let {
                binding.userAge.text = "$it years"
            }
            binding.userEmail.text = user.email
            binding.userAbout.text = user.about
            binding.userUsername.text = user.username

            sharedPreferences.edit()
                .putString(Constants.Name, user.name)
                .putString(Constants.Username, user.username)
                .putInt(Constants.Age, user.age ?: -1)
                .putString(Constants.Email, user.email)
                .putString(Constants.About, user.about)
                .apply()
        }

        binding.profileEditFab.setOnClickListener {
            if(!App.isOnline(requireActivity())) {
                Toast.makeText(requireContext(),"Please connect to the internet to perform any edits",Toast.LENGTH_SHORT).show()
            }
            else if(isEditing)
            {
                val name = binding.userNameEdit.text.toString()
                val age = binding.userAgeEdit.text.toString()
                val about = binding.userAboutEdit.text.toString()

                if(name.isEmpty() || age.isEmpty()) {
                    Toast.makeText(requireContext(),"One or more fields are empty",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                profileViewModel.updateUser(name, if(age.isEmpty()) null else age.toInt(), about) {
                    if(it.isSuccessful) {
                        Toast.makeText(requireContext(),"Changes saved",Toast.LENGTH_SHORT).show()
                        closeEditingViews()
                    }
                    else {
                        it.exception?.printStackTrace()
                        Toast.makeText(requireContext(),it.exception?.message ?: "",Toast.LENGTH_SHORT).show()
                    }
                }
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

        binding.userAboutEdit.setText(binding.userAbout.text)
        binding.userAbout.visibility = View.GONE
        binding.userAboutEditLayout.visibility = View.VISIBLE

        binding.userUsername.visibility = View.GONE
    }

}