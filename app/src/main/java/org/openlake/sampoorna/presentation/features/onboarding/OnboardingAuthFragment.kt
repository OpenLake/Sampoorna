package org.openlake.sampoorna.presentation.features.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.FragmentOnboardingAuthBinding

@AndroidEntryPoint
class OnboardingAuthFragment : Fragment() {
    private var _binding : FragmentOnboardingAuthBinding? = null
    private val binding
    get() = _binding!!
    private lateinit var authViewModel: AuthViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingAuthBinding.inflate(inflater,container,false)

        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        binding.usernameInput.doOnTextChanged { text, start, before, count ->
            if(text.toString().isEmpty()) binding.usernameInput.error = "Please enter a username"
            else binding.usernameInput.error = null

            authViewModel.updateUsername(text.toString())
        }

        binding.passwordInput.doOnTextChanged { text, start, before, count ->
            if(text.toString().isEmpty()) binding.passwordInput.error = "Please enter a password"
            else binding.passwordInput.error = null

            authViewModel.updatePassword(text.toString())
        }

        binding.authLogin.setOnClickListener {
            authViewModel.signIn()
        }

        Toast.makeText(context, getText(R.string.do_not_worry_data_is_safe), Toast.LENGTH_SHORT)
            .show()

        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}