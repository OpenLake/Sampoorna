package org.openlake.sampoorna.presentation.features.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.openlake.sampoorna.R

class OnboardingViewPagerAdapter (fragmentActivity: FragmentActivity):
    FragmentStateAdapter(fragmentActivity) {
    private val size = 4
    private val welcomeFragment = OnboardingWelcomeFragment()
    private val userInfoFragment = OnboardingUserInfoFragment()
    private val periodTrackingInfoFragment = OnboardingPeriodTrackingInfoFragment()
    private val sosInfoFragment = OnboardingSosInfoFragment()
    private val authFragment = OnboardingAuthFragment()

    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                welcomeFragment
            }
            1 -> {
                periodTrackingInfoFragment
            }
            2 -> {
                sosInfoFragment
            }
            3 -> {
                authFragment
            }
            else ->{
                welcomeFragment
            }
        }
    }

}