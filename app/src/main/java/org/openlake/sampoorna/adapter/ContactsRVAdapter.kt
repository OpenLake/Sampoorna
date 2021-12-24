package org.openlake.sampoorna.adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.openlake.sampoorna.R
import org.openlake.sampoorna.models.Contacts

class ContactsRVAdapter(val context:Context, val listener: Listeners):RecyclerView.Adapter<ContactsRVAdapter.ContactsViewHolder>() {

    val allContacts = ArrayList<Contacts>()

    inner class ContactsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val contactName = itemView.findViewById<TextView>(R.id.contact_name)
        val contactImage = itemView.findViewById<ImageView>(R.id.contact_pfp)
        val contactNumber = itemView.findViewById<TextView>(R.id.contact_number)
        val contactDelete=itemView.findViewById<ImageView>(R.id.contact_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val viewHolder = ContactsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contacts,parent, false))
        viewHolder.contactDelete.setOnClickListener {
            listener.onItemClicked(allContacts[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = allContacts[position]
        holder.contactName.text = contact.name
        holder.contactNumber.text = contact.contact.toString()


    }

    override fun getItemCount(): Int {
        return allContacts.size
    }
}
interface Listeners{
    fun onItemClicked(contact:Contacts)
}