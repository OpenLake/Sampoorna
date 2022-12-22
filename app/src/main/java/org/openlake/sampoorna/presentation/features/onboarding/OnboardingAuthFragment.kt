package org.openlake.sampoorna.presentation.features.onboarding

import android.content.Intent
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
import org.openlake.sampoorna.presentation.MainActivity

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

        binding.emailInput.doOnTextChanged { text, start, before, count ->
            if(text.toString().isEmpty()) binding.emailInput.error = "Please enter an email"
            else binding.emailInput.error = null

            authViewModel.updateEmail(text.toString())
        }

        binding.nameInput.doOnTextChanged { text, start, before, count ->
            if(text.toString().isEmpty()) binding.nameInput.error = "Please enter a name"
            else binding.nameInput.error = null

            authViewModel.updateName(text.toString())
        }

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

        binding.loginChange.setOnClickListener {
            binding.nameInput.setText("")
            binding.usernameInput.setText("")
            binding.passwordInput.setText("")
            binding.emailInput.setText("")

            authViewModel.updateName("")
            authViewModel.updateUsername("")
            authViewModel.updatePassword("")
            authViewModel.updateEmail("")

            binding.registerLayout.visibility = View.VISIBLE
            binding.authLogin.visibility = View.VISIBLE

            binding.loginLayout.visibility = View.GONE
            binding.authRegister.visibility = View.GONE
            binding.nameInput.visibility = View.GONE
            binding.usernameInput.visibility = View.GONE
        }

        binding.registerChange.setOnClickListener {
            binding.nameInput.setText("")
            binding.usernameInput.setText("")
            binding.passwordInput.setText("")
            binding.emailInput.setText("")

            authViewModel.updateName("")
            authViewModel.updateUsername("")
            authViewModel.updatePassword("")
            authViewModel.updateEmail("")

            binding.registerLayout.visibility = View.GONE
            binding.authLogin.visibility = View.GONE

            binding.loginLayout.visibility = View.VISIBLE
            binding.authRegister.visibility = View.VISIBLE
            binding.nameInput.visibility = View.VISIBLE
            binding.usernameInput.visibility = View.VISIBLE
        }

        binding.authLogin.setOnClickListener {
            if(binding.emailInput.text.toString().isEmpty() || binding.passwordInput.text.toString().isEmpty())
                return@setOnClickListener
            authViewModel.signIn {
                if(it.isSuccessful) {
                    Toast.makeText(context, "Logged in successfully!", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                }
                else {
                    it.exception?.printStackTrace()
                    Toast.makeText(requireContext(), it.exception?.message ?: "", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.authRegister.setOnClickListener {
            if(binding.emailInput.text.toString().isEmpty() || binding.passwordInput.text.toString().isEmpty() || binding.nameInput.text.toString().isEmpty() || binding.usernameInput.text.toString().isEmpty())
                return@setOnClickListener
            authViewModel.register {
                if(it.isSuccessful) {
                    authViewModel.addUser { task->
                        if(task.isSuccessful) {
                            Toast.makeText(context, "Registered successfully!", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            requireActivity().startActivity(intent)
                            requireActivity().finish()
                        }
                        else {
                            it.exception?.printStackTrace()
                            Toast.makeText(requireContext(), it.exception?.message ?: "", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    it.exception?.printStackTrace()
                    Toast.makeText(requireContext(), it.exception?.message ?: "", Toast.LENGTH_SHORT).show()
                }
            }
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