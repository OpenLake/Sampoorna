package org.openlake.sampoorna.presentation.features.contacts

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.sources.entities.Contact

class AddContactBottomSheet: BottomSheetDialogFragment() {
    private val contactSharedViewModel: ContactsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_add_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btn_save_contact: MaterialButton =view.findViewById(R.id.btn_add_contact)
        val contact_first_name:TextInputEditText=view.findViewById(R.id.first_name_input)
        val contact_last_name:TextInputEditText=view.findViewById(R.id.last_name_input)
        val contact_phone_number:TextInputEditText=view.findViewById(R.id.contact_number_add)

        //Hide Keyboard on Enter for contact_phone_number
        contact_phone_number.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view,keyCode) }

        //Save button listens handle
        btn_save_contact.setOnClickListener {
            if(contact_first_name.text?.isEmpty()!!  ){
                contact_first_name.requestFocus()
                contact_first_name.error="Please Enter Name Properly"
                return@setOnClickListener
            }
            if( contact_last_name.text?.isEmpty()!!){
                contact_last_name.setText("")

            }
            if (contact_phone_number.text?.isEmpty()!! or !Patterns.PHONE.matcher(contact_phone_number.text.toString()).matches()){
                contact_phone_number.requestFocus()
                contact_phone_number.error="Please enter phone number correctly"
                return@setOnClickListener
            }
            val contact= Contact(contact_first_name.text.toString()+" "+contact_last_name.text.toString(),contact_phone_number.text.toString())
            contactSharedViewModel.insertContact(contact)
            contact_first_name.setText("")
            contact_last_name.setText("")
            contact_phone_number.setText("")
            Toast.makeText(context, "Contact added successfully.", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
    private fun handleKeyEvent(view: View, keyCode: Int):Boolean{
        if (keyCode == KeyEvent.KEYCODE_ENTER){
            //Hide Keyboard
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0 )
            return true
        }
        return false
    }
    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
}