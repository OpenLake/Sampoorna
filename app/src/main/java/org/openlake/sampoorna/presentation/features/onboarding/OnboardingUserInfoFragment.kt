package org.openlake.sampoorna.presentation.features.onboarding


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.OnboardingUserInfoBinding
import org.openlake.sampoorna.presentation.MainActivity
import org.openlake.sampoorna.presentation.features.userFeatures.UserViewModel

@AndroidEntryPoint
class OnboardingUserInfoFragment:Fragment(R.layout.onboarding_user_info){
    private var _binding: OnboardingUserInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel:UserViewModel
    private lateinit var sharedPreferences :SharedPreferences
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

        //As the fab is clicked , we check if all values in fragment were filled or not
        val fab = activity?.findViewById<FloatingActionButton>(R.id.goNextButton)
        fab?.setOnClickListener {
                if (!binding.etName.text.isNullOrEmpty() && !binding.etAge.text.isNullOrEmpty() && !binding.etResidence.text.isNullOrEmpty() && !binding.etEmail.text.isNullOrEmpty()){
                    sharedPreferences= activity?.getSharedPreferences("login",Context.MODE_PRIVATE)!!
                    if (sharedPreferences.contains("username")){
                        startActivity(Intent(context, MainActivity::class.java))
                        //if all fields were filled , move to Main Activity
                        requireActivity().finish()
                    }
            }
            else{
                //else raise a toast
                Toast.makeText(context, "Please fill details", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}