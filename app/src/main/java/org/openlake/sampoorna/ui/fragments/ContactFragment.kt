package org.openlake.sampoorna.ui.fragments

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
import org.openlake.sampoorna.adapter.ContactsRVAdapter
import org.openlake.sampoorna.adapter.Listeners
import org.openlake.sampoorna.databinding.FragmentContactBinding
import org.openlake.sampoorna.models.Contacts
import org.openlake.sampoorna.viewmodel.ContactsViewModel

@AndroidEntryPoint
class ContactFragment : Fragment(R.layout.fragment_contact), Listeners {
    lateinit var viewModel: ContactsViewModel
    private var _binding:FragmentContactBinding? = null
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
        viewModel = ViewModelProvider(this)[ContactsViewModel::class.java]
        viewModel.allContacts.observe(viewLifecycleOwner, { it->
            it?.let{
               // adapter.updateContacts(it)
            }
        })

        //fab actions
        val addContactBtn = view.findViewById<FloatingActionButton>(R.id.contacts_add)
        addContactBtn.setOnClickListener {
            Log.d("FAB","works")
        }
        return view
    }

    override fun onItemClicked(contact: Contacts) {
        viewModel.deleteContact(contact)
    }
}