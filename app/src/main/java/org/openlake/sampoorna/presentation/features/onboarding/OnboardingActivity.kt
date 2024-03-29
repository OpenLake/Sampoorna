package org.openlake.sampoorna.presentation.features.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.ActivityOnboardingBinding
import org.openlake.sampoorna.presentation.MainActivity
import org.openlake.sampoorna.presentation.features.userFeatures.UserViewModel

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private var onboardingViewPagerAdapter: OnboardingViewPagerAdapter? = null
    private lateinit var userViewModel: UserViewModel
    private lateinit var authViewModel: AuthViewModel

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

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

        binding.goNextButton.setOnClickListener {
            if(binding.slider.currentItem==3)
                return@setOnClickListener

            binding.slider.currentItem += 1
        }

        binding.slider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if(position == 3) {
                    binding.goNextButton.visibility = View.GONE
                }
                else {
                    binding.goNextButton.visibility = View.VISIBLE
                }
                super.onPageSelected(position)
            }
        })

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