package org.openlake.sampoorna.presentation.features.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.openlake.sampoorna.R

class OnboardingViewPagerAdapter (fragmentActivity: FragmentActivity):
    FragmentStateAdapter(fragmentActivity) {
    private val layouts = arrayOf(R.layout.onboarding_welcome,R.layout.onboarding_period_tracking_info,R.layout.onboarding_sos_info,R.layout.onboarding_user_info)
    private val welcomeFragment = OnboardingWelcomeFragment()
    private val userInfoFragment = OnboardingUserInfoFragment()
    private val periodTrackingInfoFragment = OnboardingPeriodTrackingInfoFragment()
    private val sosInfoFragment = OnboardingSosInfoFragment()

    override fun getItemCount(): Int {
        return layouts.size
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
                userInfoFragment
            }
            else ->{
                welcomeFragment
            }
        }
        }

}