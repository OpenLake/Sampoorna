package org.openlake.sampoorna.presentation.features.sos_alert

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.sources.entities.Contact
import org.openlake.sampoorna.data.sources.entities.User
import org.openlake.sampoorna.databinding.FragmentAlertBinding
import org.openlake.sampoorna.presentation.features.sos_message.SosMessageBottomSheet
import org.openlake.sampoorna.presentation.features.userFeatures.UserViewModel
import org.openlake.sampoorna.util.Variables
import org.openlake.sampoorna.util.services.FetchLocation

@AndroidEntryPoint
class AlertFragment : Fragment(R.layout.fragment_alert) {
    private lateinit var userViewModel: UserViewModel

    companion object{
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.SEND_SMS
        )
    }
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

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.insertUser(User("DummyForNow","O+","Uttarakhand","wait","dunno"))
      userViewModel.allContacts.observe(viewLifecycleOwner,{listOfContacts->
          //SOS Button click handling
          sosButton.setOnClickListener {
               checkPermissionsGiven(it,listOfContacts)
          }
        })
        // Edit SOS Message Click Handling
        sosMessage.setOnClickListener {
            addSOSMessage.show(parentFragmentManager,"addSOSMessage")
        }
        //Navigating to Contacts List
        gotoContacts.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_alertFragment_to_contactFragment)
        }
        return view
    }
    @SuppressLint("MissingPermission")
    private fun checkPermissionsGiven(view: View,list:List<Contact>?){
        if ((ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)){
            //do some work
                //NOTE: Check if location is turned on
            if (list != null) {
                for (contact in list) {
                    val smsManager = SmsManager.getDefault()
                    val latlong =  FetchLocation.getLastLocation(requireActivity())
                    val vars = Variables()
                    vars.location = latlong
                    userViewModel.userDetails.observe(viewLifecycleOwner,{
                        smsManager.sendTextMessage(contact.contact, null, "${it[0].sosMessage}+${vars.location}", null, null)

                    })
                }
            }
        }
        else{
            requestLocationPermission(view)
        }
    }
    private fun requestLocationPermission(view: View) {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION) && shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
            view.showSnackbar(
                view,
                getString(R.string.permission_required),
                Snackbar.LENGTH_INDEFINITE,
                getString(R.string.ok)
            ) {
                    launchMultiPermission()
            }
        }
        else{
            launchMultiPermission()
        }
    }

    private fun launchMultiPermission(){
        requestMultiplePermissions.launch(
            PERMISSIONS
        )
    }
    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        for (entry in permissions.entries){
            Toast.makeText(requireActivity(), "${entry.key} = ${entry.value}", Toast.LENGTH_SHORT).show()
            //ask for each individual permission
            if (!entry.value){
                requestIndividualPermissionLauncher.launch(
                    entry.key
                )
            }
        }
    }
    private val requestIndividualPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
                isGranted:Boolean->
            if (isGranted){
                Log.i("Permission","Granted")
            }
            else{
                Log.i("Permission","Denied")
            }
            }


    private fun View.showSnackbar(
        view: View,
        msg: String,
        length: Int,
        actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this)
            }.show()
        } else {
            snackbar.show()
        }
    }

}
