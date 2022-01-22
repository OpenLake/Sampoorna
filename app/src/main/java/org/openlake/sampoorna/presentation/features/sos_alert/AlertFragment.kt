package org.openlake.sampoorna.presentation.features.sos_alert

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.FragmentAlertBinding
import org.openlake.sampoorna.presentation.features.sos_message.SosMessageBottomSheet
import org.openlake.sampoorna.presentation.features.userFeatures.UserViewModel
import org.openlake.sampoorna.util.services.ReactivateService
import org.openlake.sampoorna.util.services.SOSHelper
import org.openlake.sampoorna.util.services.SOSService

@AndroidEntryPoint
class AlertFragment : Fragment(R.layout.fragment_alert) {
    private lateinit var userViewModel: UserViewModel
    lateinit var sharedPreferences : SharedPreferences
    lateinit var contactsListPreferences: SharedPreferences
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
        //Shared preferences to get contacts list
        contactsListPreferences = requireActivity().getSharedPreferences("sosContacts",Context.MODE_PRIVATE)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.allContacts.observe(viewLifecycleOwner,{listOfContacts->

            val setContacts = mutableSetOf<String>()

            for (contact in listOfContacts){
                contact.contact?.let { setContacts.add(it) }
            }

            val editor:SharedPreferences.Editor = contactsListPreferences.edit()
            editor.putStringSet("contacts",setContacts)
                .apply()


          //SOS Button click handling
          sosButton.setOnClickListener {
              if (listOfContacts.isNullOrEmpty()){
                  Toast.makeText(context, "No contacts added yet.", Toast.LENGTH_SHORT).show()
              }
             // SOSHelper.sendSms(listOfContacts,userViewModel,viewLifecycleOwner, requireActivity())
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
