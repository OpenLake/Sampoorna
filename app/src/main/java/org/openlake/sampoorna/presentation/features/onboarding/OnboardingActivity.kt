package org.openlake.sampoorna.presentation.features.onboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.sources.entities.User
import org.openlake.sampoorna.databinding.ActivityOnboardingBinding
import org.openlake.sampoorna.presentation.MainActivity
import org.openlake.sampoorna.presentation.features.userFeatures.UserViewModel

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private var tabLayout: TabLayout? = null
    var onboardingViewPagerAdapter: OnboardingViewPagerAdapter? = null
    lateinit var onBoardingViewPager: ViewPager2
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        //instantiating Shared Preferences
        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        //checking if SharedPreferences contains username value or not
        if (sharedPreferences.contains("username")) {
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
                onBoardingViewPager.currentItem += 1
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
        //setting up tab layout , viewpager , viewpager adapter
        tabLayout = findViewById(R.id.tab_indicator)
        onBoardingViewPager = findViewById(R.id.slider)
        onboardingViewPagerAdapter = OnboardingViewPagerAdapter(this)
        onBoardingViewPager.adapter = onboardingViewPagerAdapter
        TabLayoutMediator(tabLayout!!, onBoardingViewPager) { _, _ -> }.attach()
    }

    override fun onBackPressed() {
        if (onBoardingViewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            onBoardingViewPager.currentItem -= 1
        }
    }
}