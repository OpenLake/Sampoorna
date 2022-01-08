package org.openlake.sampoorna.presentation.features.contacts

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.sources.entities.Contact
import javax.inject.Inject

class ContactsRVAdapter @Inject constructor(val context:Context, private val listener: Listeners):RecyclerView.Adapter<ContactsRVAdapter.ContactsViewHolder>() {

    private var allContacts =ArrayList<Contact>()

    inner class ContactsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val contactName: TextView = itemView.findViewById(R.id.contact_name)
        val contactNumber: TextView = itemView.findViewById(R.id.contact_number)
        val contactDelete: ImageView = itemView.findViewById(R.id.contact_delete)
        init {
            contactDelete.setOnClickListener {
                listener.onItemClicked(allContacts[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_contacts, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact=allContacts[position]
        holder.contactName.text = contact.name.toString()
        holder.contactNumber.text = contact.contact.toString()
    }

    override fun getItemCount(): Int {
        return allContacts.size
    }

    fun updateContacts(listofContacts:ArrayList<Contact>){
        if (allContacts.isEmpty()){
            allContacts.addAll(listofContacts)
        }
        allContacts.clear()
        allContacts.addAll(listofContacts)
        Log.d("ListofC","${allContacts.size}")
        notifyDataSetChanged()
    }
}
interface Listeners{
    fun onItemClicked(contact: Contact)
}