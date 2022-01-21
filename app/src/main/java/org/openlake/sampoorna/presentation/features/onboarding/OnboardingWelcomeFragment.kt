package org.openlake.sampoorna.presentation.features.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.OnboardingWelcomeBinding

class OnboardingWelcomeFragment: Fragment(R.layout.onboarding_welcome) {
    private var _binding:OnboardingWelcomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OnboardingWelcomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
  }