package org.openlake.sampoorna.presentation.features.onboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.auth.AuthResult
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

    override fun onCreate(savedInstanceState: Bundle?) {
        //instantiating Shared Preferences
        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)

        //checking if SharedPreferences contains username value or not
        if (sharedPreferences.contains("username")) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // As in the manifest, we have changed application theme to SplashTheme.
        // Hence, we need to switch back to our main theme.
        setTheme(R.style.Theme_Sampoorna)

        super.onCreate(savedInstanceState)

        //binding setup
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //instantiating UserViewModel in which we will enter User
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        userViewModel.userDetails.observe(this) {
            //viewpager fab button listening..
            binding.goNextButton.setOnClickListener {
                if(binding.slider.currentItem==3){
                    userViewModel.userInfoSubmitted.postValue(true)
                    authViewModel.signIn()
                }
                binding.slider.currentItem += 1
                if (sharedPreferences.contains("username")) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            if (it.isEmpty()) {
                userViewModel.insertUser(User(null, null, null, null, null, null, 0))
            } else {
                if (it.first().name != null && it.first().address != null && it.first().age != null && it.first().email != null) {
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("username", it[0].name)
                    editor.apply()
                }
            }
        }

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        authViewModel.authResult.observe(this){ result->
            when(result){
                is AuthResult.Authorized ->{
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is AuthResult.Unauthorized -> {
                    Toast.makeText(this,"Authorization failed, please check your username and password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this,"Unknown error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }

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