package org.openlake.sampoorna.presentation.features.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.OnboardingPeriodTrackingInfoBinding

class OnboardingPeriodTrackingInfoFragment: Fragment(R.layout.onboarding_period_tracking_info){
    private var _binding : OnboardingPeriodTrackingInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OnboardingPeriodTrackingInfoBinding.inflate(inflater,container,false)
        return binding.root
    }
}