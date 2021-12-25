package org.openlake.sampoorna.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.material.transition.MaterialFadeThrough
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.FragmentAlertBinding

class AlertFragment : Fragment(R.layout.fragment_alert) {
    //enabling data binding
    private var _binding: FragmentAlertBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
       }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertBinding.inflate(inflater,container,false)
        val view = binding.root
        val gotoContacts = binding.icContacts
        gotoContacts.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_alertFragment_to_contactFragment)
        }
        return view
    }
}