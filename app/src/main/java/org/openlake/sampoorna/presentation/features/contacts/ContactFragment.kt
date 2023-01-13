package org.openlake.sampoorna.presentation.features.contacts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.sources.entities.Contact
import org.openlake.sampoorna.databinding.FragmentContactBinding

@AndroidEntryPoint
class ContactFragment : Fragment(R.layout.fragment_contact), Listeners {

    private lateinit var contactsViewModel: ContactsViewModel
    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentContactBinding.inflate(inflater,container,false)
        val view = binding.root

        //Implementing Recycler View settings
        val contactsRV = view.findViewById<RecyclerView>(R.id.contacts_rv)
        contactsRV.layoutManager = LinearLayoutManager(activity)
        val adapter = ContactsRVAdapter(view.context,this)
        contactsRV.adapter = adapter

        //ViewModel instantiating
        contactsViewModel = ViewModelProvider(this)[ContactsViewModel::class.java]
        contactsViewModel.allContacts.observe(viewLifecycleOwner
        ) {
            //This is problematic:
            Log.d("it", "$it")
            if (it != null) {
                adapter.updateContacts(it as ArrayList<Contact>)
            }
        }
        //fab actions
        val addContactsFragment= AddContactBottomSheet()
        val addContactBtn = view.findViewById<FloatingActionButton>(R.id.contacts_add)
        addContactBtn.setOnClickListener {
            if(addContactsFragment.isAdded){
                return@setOnClickListener
            }
            addContactsFragment.show(parentFragmentManager,"addContactBottomSheet")
        }
        return view
    }

    override fun onItemClicked(contact: Contact) {
        contactsViewModel.deleteContact(contact)
        Log.d("is it working?","$contact")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}