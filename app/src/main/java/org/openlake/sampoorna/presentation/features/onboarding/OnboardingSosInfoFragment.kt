package org.openlake.sampoorna.presentation.features.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.openlake.sampoorna.databinding.OnboardingSosInfoBinding

class OnboardingSosInfoFragment: Fragment() {

    private var _binding: OnboardingSosInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OnboardingSosInfoBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}