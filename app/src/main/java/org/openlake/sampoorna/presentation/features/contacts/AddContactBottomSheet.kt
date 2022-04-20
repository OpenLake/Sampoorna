package org.openlake.sampoorna.presentation.features.contacts

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
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
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.sources.entities.Contact

class AddContactBottomSheet : BottomSheetDialogFragment(),LoaderManager.LoaderCallbacks<Cursor> {
    private val contactSharedViewModel: ContactsViewModel by activityViewModels()
    private lateinit var contactAdapter : SimpleAdapter
    private lateinit var contactName : AutoCompleteTextView
    private lateinit var contactPhoneNumber : AutoCompleteTextView
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
        contactName = view.findViewById(R.id.name_input)
        contactPhoneNumber = view.findViewById(R.id.contact_number_add)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED)
        {
            loaderManager.initLoader(0,null,this)
        }
        //Hide Keyboard on Enter for contact_phone_number
        contactPhoneNumber.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode) }


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
        contactName.onItemClickListener = contactListener
        contactPhoneNumber.onItemClickListener = contactListener

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

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER+" = 1",
            null,
            ContactsContract.CommonDataKinds.Phone.MIMETYPE
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        val contacts = mutableListOf<Map<String,String>>()
        while(cursor?.moveToNext() == true)
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
        }
        contactAdapter = SimpleAdapter(
            requireContext(),
            contacts,
            R.layout.contact_dropdown_item,
            arrayOf("name","number"),
            intArrayOf(R.id.contact_dropdown_name,R.id.contact_dropdown_number)
        )
        contactName.setAdapter(contactAdapter)
        contactPhoneNumber.setAdapter(contactAdapter)
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
    }
}