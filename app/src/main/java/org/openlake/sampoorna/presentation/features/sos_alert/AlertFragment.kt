package org.openlake.sampoorna.presentation.features.sos_alert

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.App
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.FragmentAlertBinding
import org.openlake.sampoorna.presentation.MainActivity.Companion.SOSSwitch
import org.openlake.sampoorna.presentation.features.sos_message.SosMessageBottomSheet
import org.openlake.sampoorna.presentation.features.userFeatures.UserViewModel
import org.openlake.sampoorna.util.services.ReactivateService

@AndroidEntryPoint
class AlertFragment : Fragment(R.layout.fragment_alert) {
    private lateinit var userViewModel: UserViewModel
    lateinit var contactsListPreferences: SharedPreferences
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.SEND_SMS
    )
    private val REQUEST_CODE = 101

    //enabling data binding
    private var _binding: FragmentAlertBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertBinding.inflate(inflater, container, false)
        val view = binding.root

        val gotoContacts = binding.icContacts
        val sosMessage = binding.icMessage
        val sosButton = binding.sosbutton
        val addSOSMessage = SosMessageBottomSheet()
        val helloUser = binding.hellouser

        //Shared preferences to get contacts list
        contactsListPreferences =
            requireActivity().getSharedPreferences("sosContacts", Context.MODE_PRIVATE)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val dialogView = inflater.inflate(R.layout.complete_profile_dialog, container, false)
        val completeDialog = AlertDialog.Builder(requireActivity())
            .setView(dialogView)
            .create()
        completeDialog.setOnShowListener { dialogInterface->
            val completeButton = dialogView.findViewById<Button>(R.id.complete_positive)
            val rejectButton = dialogView.findViewById<Button>(R.id.complete_negative)

            completeButton.setOnClickListener {
                view.findNavController().navigate(R.id.profileFragment)
                dialogInterface.cancel()
            }

            rejectButton.setOnClickListener {
                dialogInterface.cancel()
            }
        }

        userViewModel.getUser()
        userViewModel.user.observe(viewLifecycleOwner){ user->
            helloUser.text = getString(R.string.hello) + user.name

            if((user.age == null || user.about.isEmpty()) && App.isOnline(requireActivity())) {
                if(!completeDialog.isShowing) {
                    completeDialog.show()
                }
            }
        }

        userViewModel.allContacts.observe(viewLifecycleOwner) { listOfContacts ->

            val setContacts = mutableSetOf<String>()

            for (contact in listOfContacts) {
                contact.contact?.let { setContacts.add(it) }
            }

            val editor: SharedPreferences.Editor = contactsListPreferences.edit()
            editor.putStringSet("contacts", setContacts)
                .apply()

            //SOS Button click handling
            SOSSwitch.observe(requireActivity()) { bool ->
                sosButton.setOnClickListener {
                    askForPermissions(requireActivity())
                    if (bool) {
                        Toast.makeText(context, getString(R.string.sos_service_stoped), Toast.LENGTH_SHORT).show()
                        SOSSwitch.postValue(false)
                        val broadcastIntent = Intent()
                        broadcastIntent.action = "restartService"
                        broadcastIntent.setClass(
                            requireActivity(),
                            ReactivateService::class.java
                        )
                        requireActivity().sendBroadcast(broadcastIntent)

                    } else {
                        SOSSwitch.postValue(true)
                        Toast.makeText(context, getString(R.string.sos_service_started), Toast.LENGTH_SHORT).show()
                        val locationSetting =
                            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        if (locationSetting.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            //Start SOS Service here
                            val broadcastIntent = Intent()
                            broadcastIntent.action = "restart Service"
                            broadcastIntent.setClass(
                                requireActivity(),
                                ReactivateService::class.java
                            )
                            requireActivity().sendBroadcast(broadcastIntent)
                            if (listOfContacts.isNullOrEmpty()) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.no_contacted_added),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.turn_on_location_message),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        // Edit SOS Message Click Handling
        sosMessage.setOnClickListener {
            if (addSOSMessage.isAdded) {
                return@setOnClickListener
            }
            addSOSMessage.show(parentFragmentManager, "addSOSMessage")
        }

        //Navigating to Contacts List
        gotoContacts.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_alertFragment_to_contactFragment)
        }

        return view
    }

    private fun askForPermissions(activity: Activity): Boolean {
        val permissionsToRequest: MutableList<String> = ArrayList()
        for (permission in PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.isEmpty()) {
            return false
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity, permissionsToRequest.toTypedArray(),
                REQUEST_CODE
            )
        }
        return true
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
