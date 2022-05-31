package org.openlake.sampoorna.presentation.features.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.OnboardingUserInfoBinding
import org.openlake.sampoorna.presentation.features.userFeatures.UserViewModel

@AndroidEntryPoint
class OnboardingUserInfoFragment : Fragment(R.layout.onboarding_user_info) {
    private var _binding: OnboardingUserInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OnboardingUserInfoBinding.inflate(inflater, container, false)
        //instantiating User ViewModel
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        //Checking Edit Texts values

        binding.etName.doOnTextChanged { text, start, before, count ->

            if(text.toString().isEmpty()) binding.etName.error = "Please enter your name"
            else binding.etName.error = null

            userViewModel.updateUserName(text.toString())
        }
        binding.etAge.doOnTextChanged { text, start, before, count ->

            if(text.toString().isEmpty()) binding.etAge.error = "Please enter your age"
            else binding.etAge.error = null

            if(text.toString().isNotEmpty()) userViewModel.updateAge(text.toString().trim().toInt())
        }
        binding.etEmail.doOnTextChanged { text, start, before, count ->

            if(text.toString().isEmpty()) binding.etEmail.error = "Please enter your email"
            else binding.etEmail.error = null

            userViewModel.updateEmail(text.toString())
        }
        binding.etResidence.doOnTextChanged { text, start, before, count ->

            if(text.toString().isEmpty()) binding.etResidence.error = "Please enter your address"
            else binding.etResidence.error = null

            userViewModel.updateAddress(text.toString())
        }
        userViewModel.userInfoSubmitted.observe(viewLifecycleOwner){
            if(it)
            {
                if(binding.etName.text.toString().isEmpty()) binding.etName.error = "Please enter your name"
                else binding.etName.error = null

                if(binding.etAge.text.toString().isEmpty()) binding.etAge.error = "Please enter your age"
                else binding.etAge.error = null

                if(binding.etEmail.text.toString().isEmpty()) binding.etEmail.error = "Please enter your email"
                else binding.etEmail.error = null

                if(binding.etResidence.text.toString().isEmpty()) binding.etResidence.error = "Please enter your address"
                else binding.etResidence.error = null

                userViewModel.userInfoSubmitted.postValue(false)
            }
        }
        Toast.makeText(context, getText(R.string.do_not_worry_data_is_safe), Toast.LENGTH_SHORT)
            .show()
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}