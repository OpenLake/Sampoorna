package org.openlake.sampoorna.presentation.features.sos_alert

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.FragmentAlertBinding
import org.openlake.sampoorna.presentation.features.sos_message.SosMessageBottomSheet
import org.openlake.sampoorna.presentation.features.userFeatures.UserViewModel
import org.openlake.sampoorna.util.services.SOSHelper

@AndroidEntryPoint
class AlertFragment : Fragment(R.layout.fragment_alert) {
    private lateinit var userViewModel: UserViewModel
    lateinit var sharedPreferences : SharedPreferences
    val user:String = "User"
    //enabling data binding
    private var _binding: FragmentAlertBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlertBinding.inflate(inflater,container,false)
        val view = binding.root
        val gotoContacts = binding.icContacts
        val sosMessage = binding.icMessage
        val sosButton = binding.sosbutton
        val addSOSMessage = SosMessageBottomSheet()
        val helloUser = binding.hellouser
        //Using SharedPreferences to get username
        sharedPreferences = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE)
        helloUser.text = "Hello, " +  sharedPreferences.getString("username","")

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.allContacts.observe(viewLifecycleOwner,{listOfContacts->
          //SOS Button click handling
          sosButton.setOnClickListener {
              if (listOfContacts.isNullOrEmpty()){
                  Toast.makeText(context, "No contacts added yet.", Toast.LENGTH_SHORT).show()
              }
              SOSHelper.sendSms(listOfContacts,userViewModel,viewLifecycleOwner, requireActivity())
          }
        }
      )
        // Edit SOS Message Click Handling
        sosMessage.setOnClickListener {
            if (addSOSMessage.isAdded){
                return@setOnClickListener
            }
            addSOSMessage.show(parentFragmentManager,"addSOSMessage")
        }
        //Navigating to Contacts List
        gotoContacts.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_alertFragment_to_contactFragment)
        }
          return view
    }

}
