package org.openlake.sampoorna.presentation.features.contacts

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.sources.entities.Contacts
import java.util.regex.Pattern

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

        val btn_save_contact:MaterialButton=view.findViewById(R.id.btn_add_contact)
        val contact_first_name:TextInputEditText=view.findViewById(R.id.first_name_input)
        val contact_last_name:TextInputEditText=view.findViewById(R.id.last_name_input)
        val contact_phone_number:TextInputEditText=view.findViewById(R.id.contact_number_add)
        btn_save_contact.setOnClickListener {
            if(contact_first_name.text?.isEmpty()!!  ){
                contact_first_name.error="Please Enter Name Properly"
                contact_first_name.requestFocus()
                return@setOnClickListener
            }
            if( contact_last_name.text?.isEmpty()!!){
                contact_last_name.setText("")

            }
            if (contact_phone_number.text?.isEmpty()!! or !Patterns.PHONE.matcher(contact_phone_number.text.toString()).matches()){
                contact_phone_number.error="Please enter phone number correctly"
                contact_phone_number.requestFocus()
                return@setOnClickListener
            }
            val contact= Contacts(contact_first_name.text.toString()+contact_last_name.toString(),contact_phone_number.text.toString())
            contactSharedViewModel.insertContact(contact)
            contactSharedViewModel.passContacts(contact)

            dismiss()
        }

    }

}