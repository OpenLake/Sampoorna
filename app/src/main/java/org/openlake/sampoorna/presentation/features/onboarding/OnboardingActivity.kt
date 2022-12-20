package org.openlake.sampoorna.presentation.features.onboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.sources.entities.User
import org.openlake.sampoorna.databinding.ActivityOnboardingBinding
import org.openlake.sampoorna.presentation.MainActivity
import org.openlake.sampoorna.presentation.features.userFeatures.UserViewModel

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    var onboardingViewPagerAdapter: OnboardingViewPagerAdapter? = null
    private lateinit var userViewModel: UserViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        //instantiating Shared Preferences
        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)

        // As in the manifest, we have changed application theme to SplashTheme.
        // Hence, we need to switch back to our main theme.
        setTheme(R.style.Theme_Sampoorna)

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        super.onCreate(savedInstanceState)

        //binding setup
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //instantiating UserViewModel in which we will enter User
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        userViewModel.userDetails.observe(this) {
            //viewpager fab button listening..
            binding.goNextButton.setOnClickListener {
                if(binding.slider.currentItem==3)
                    return@setOnClickListener

                binding.slider.currentItem += 1
            }
        }

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        //setting up tab layout , viewpager , viewpager adapter
        onboardingViewPagerAdapter = OnboardingViewPagerAdapter(this)
        binding.slider.adapter = onboardingViewPagerAdapter
        TabLayoutMediator(binding.tabIndicator, binding.slider) { _, _ -> }.attach()
    }

    override fun onBackPressed() {
        if (binding.slider.currentItem == 0) {
            super.onBackPressed()
        } else {
            binding.slider.currentItem -= 1
        }
    }
}