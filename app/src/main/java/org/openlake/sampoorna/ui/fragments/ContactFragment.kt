package org.openlake.sampoorna.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.FragmentContactBinding
import org.openlake.sampoorna.viewmodel.ContactsViewModel

class ContactFragment : Fragment(R.layout.fragment_contact) {
        lateinit var viewModel: ContactsViewModel
        private var _binding:FragmentContactBinding? = null
        private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(activity?.application!!)).get(ContactsViewModel)
        viewModel.allContacts.observe(this, Observer {
            v
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

}