package org.openlake.sampoorna.presentation.features.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.openlake.sampoorna.databinding.OnboardingPeriodTrackingInfoBinding

class OnboardingPeriodTrackingInfoFragment: Fragment(){

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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}