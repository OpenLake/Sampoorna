package org.openlake.sampoorna.presentation.features.contacts

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Patterns
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.sources.entities.Contact

class AddContactBottomSheet : BottomSheetDialogFragment(),LoaderManager.LoaderCallbacks<Cursor> {
    private val contactSharedViewModel: ContactsViewModel by activityViewModels()
    private lateinit var contactAdapter : SimpleAdapter
    private lateinit var contactName : TextInputEditText
    private lateinit var contactPhoneNumber : TextInputEditText
    private var contactUri : Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_add_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSaveContact: MaterialButton = view.findViewById(R.id.btn_add_contact)
        val btnGetContact: MaterialButton = view.findViewById(R.id.btn_get_contact)

        contactName = view.findViewById(R.id.name_input)
        contactPhoneNumber = view.findViewById(R.id.contact_number_add)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        //Hide Keyboard on Enter for contact_phone_number
        contactPhoneNumber.setOnKeyListener { v, keyCode, _ -> handleKeyEvent(v, keyCode) }


        val contactListener = AdapterView.OnItemClickListener { adapterView, _, position, _ ->
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            val m : Map<String,String> = adapterView.getItemAtPosition(position) as Map<String, String>
            val name = m["name"]
            val number = m["number"]
            contactName.setText(name)
            contactPhoneNumber.setText(number)
         }

        //Save button listens handle
        btnSaveContact.setOnClickListener {
            if (contactName.text?.isEmpty()!!) {
                contactName.requestFocus()
                contactName.error = getString(R.string._enter_name_error)
                return@setOnClickListener
            }
            if (contactPhoneNumber.text?.isEmpty()!! or !Patterns.PHONE.matcher(
                    contactPhoneNumber.text.toString()
                ).matches()
            ) {
                contactPhoneNumber.requestFocus()
                contactPhoneNumber.error = getString(R.string.enter_phone_number_error)
                return@setOnClickListener
            }
            val contact = Contact(
                contactName.text.toString(),
                contactPhoneNumber.text.toString()
            )
            contactSharedViewModel.insertContact(contact)
            contactName.setText("")
            contactPhoneNumber.setText("")
            Toast.makeText(context, getString(R.string.contact_added_sucess), Toast.LENGTH_SHORT).show()
            dismiss()
        }

        btnGetContact.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), CONTACT_REQUEST)
            }
            else
            {
                getContact()
            }
        }
    }

    private fun getContact()
    {
        val intent = Intent(Intent.ACTION_PICK,ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        startActivityForResult(intent, CONTACT_REQUEST)
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            //Hide Keyboard
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==CONTACT_REQUEST)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getContact()
            }
            else
            {
                Toast.makeText(requireContext(),"Permissions not granted",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK && requestCode== CONTACT_REQUEST && data!=null)
        {
            contactUri = data.data
            loaderManager.initLoader(0,null,this)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            contactUri!!,
            PROJECTION,
            ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER+" = 1",
            null,
            ContactsContract.CommonDataKinds.Phone.MIMETYPE
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        val contacts = mutableListOf<Map<String,String>>()
        if(cursor?.moveToNext() == true)
        {
            val name = cursor.getString(CONTACT_NAME_INDEX)
            val contact = cursor.getString(CONTACT_NUMBER_INDEX)
            var number = ""
            for(i in contact.indices)
            {
                if(contact[i]!=' ') number += contact[i]
            }
            if(number[0]=='+')
            {
                number = number.substring(3)
            }
            var found = false
            for(i in contacts.indices)
            {
                if(contacts[i]["number"]==number)
                {
                    found = true
                    break
                }
            }
            if(!found)
            {
                val m = mapOf(Pair("name",name), Pair("number",number))
                contacts.add(m)
            }
            contactName.setText(name)
            contactPhoneNumber.setText(number)
            cursor.close()
        }
        contactAdapter = SimpleAdapter(
            requireContext(),
            contacts,
            R.layout.contact_dropdown_item,
            arrayOf("name","number"),
            intArrayOf(R.id.contact_dropdown_name,R.id.contact_dropdown_number)
        )
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {

    }
    companion object{
        private val PROJECTION = arrayOf(
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        private const val CONTACT_NAME_INDEX = 1
        private const val CONTACT_NUMBER_INDEX = 2

        private const val CONTACT_REQUEST = 999
    }
}