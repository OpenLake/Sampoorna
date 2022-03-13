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
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        //Checking Edit Texts values
        binding.etName.doOnTextChanged { text, start, before, count ->
            userViewModel.updateUserName(text.toString())
        }
        binding.etAge.doOnTextChanged { text, start, before, count ->
            userViewModel.updateAge(text.toString().trim().toInt())
        }
        binding.etEmail.doOnTextChanged { text, start, before, count ->
            userViewModel.updateEmail(text.toString())
        }
        binding.etResidence.doOnTextChanged { text, start, before, count ->
            userViewModel.updateAddress(text.toString())
        }
        Toast.makeText(context, getText(R.string.do_not_worry_data_is_safe), Toast.LENGTH_SHORT)
            .show()
        return binding.root
    }
}