package org.openlake.sampoorna.presentation.features.contacts

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
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
import org.openlake.sampoorna.presentation.MainActivity

class AddContactBottomSheet : BottomSheetDialogFragment(),LoaderManager.LoaderCallbacks<Cursor> {
    private val contactSharedViewModel: ContactsViewModel by activityViewModels()
    private lateinit var cursorAdapter : SimpleCursorAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_add_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btn_save_contact: MaterialButton = view.findViewById(R.id.btn_add_contact)
        val contact_first_name: AutoCompleteTextView = view.findViewById(R.id.first_name_input)
        val contact_last_name: TextInputEditText = view.findViewById(R.id.last_name_input)
        val contact_phone_number: TextInputEditText = view.findViewById(R.id.contact_number_add)

        if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED)
        {
            loaderManager.initLoader(0,null,this)
            cursorAdapter = SimpleCursorAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,null,
                FROM_COLUMNS,
                TO_IDS,0)

            contact_first_name.setAdapter(cursorAdapter)
        }
        //Hide Keyboard on Enter for contact_phone_number
        contact_phone_number.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode) }

        //Save button listens handle
        btn_save_contact.setOnClickListener {
            if (contact_first_name.text?.isEmpty()!!) {
                contact_first_name.requestFocus()
                contact_first_name.error = getString(R.string._enter_name_error)
                return@setOnClickListener
            }
            if (contact_last_name.text?.isEmpty()!!) {
                contact_last_name.setText("")

            }
            if (contact_phone_number.text?.isEmpty()!! or !Patterns.PHONE.matcher(
                    contact_phone_number.text.toString()
                ).matches()
            ) {
                contact_phone_number.requestFocus()
                contact_phone_number.error = getString(R.string.enter_phone_number_error)
                return@setOnClickListener
            }
            val contact = Contact(
                contact_first_name.text.toString() + " " + contact_last_name.text.toString(),
                contact_phone_number.text.toString()
            )
            contactSharedViewModel.insertContact(contact)
            contact_first_name.setText("")
            contact_last_name.setText("")
            contact_phone_number.setText("")
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
        return CursorLoader(requireContext(),ContactsContract.Contacts.CONTENT_URI, PROJECTION,null,
            null,null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        while(cursor?.moveToNext() == true)
        {
            val id = cursor.getLong(CONTACT_ID_INDEX)
            val key = cursor.getString(CONTACT_KEY_INDEX)
            val name = cursor.getString(CONTACT_NAME_INDEX)
            val uri = ContactsContract.Contacts.getLookupUri(id,key)

            Log.d("phone",cursor.getString(2))
        }
        cursorAdapter.swapCursor(cursor)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        cursorAdapter.swapCursor(null)
    }
    companion object{
        private val FROM_COLUMNS = arrayOf(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
        private val TO_IDS = intArrayOf(android.R.id.text1)
        private val PROJECTION = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        )
        private const val CONTACT_ID_INDEX = 0
        private const val CONTACT_KEY_INDEX = 1
        private const val CONTACT_NAME_INDEX = 2
    }
}