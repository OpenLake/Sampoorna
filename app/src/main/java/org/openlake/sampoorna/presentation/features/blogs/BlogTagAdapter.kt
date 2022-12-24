package org.openlake.sampoorna.presentation.features.blogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.openlake.sampoorna.R

class BlogTagAdapter(val context: Context, val editing: Boolean = false): RecyclerView.Adapter<BlogTagAdapter.BlogTagViewHolder>() {

    var tagList: MutableList<String> = mutableListOf()

    set(value) {
        field = value
        notifyDataSetChanged()
    }

    inner class BlogTagViewHolder(val itemView: View): ViewHolder(itemView) {
        val tagText: TextView = itemView.findViewById(R.id.tag_text)
        val tagCross: ImageView = itemView.findViewById(R.id.tag_cross)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogTagViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.tag_card, parent, false)
        return BlogTagViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlogTagViewHolder, position: Int) {
        val tag = tagList[position]

        if(editing) {
            holder.tagCross.visibility = View.VISIBLE
        }

        holder.tagText.text = tag
        holder.tagCross.setOnClickListener {
            tagList.removeAt(position)
            notifyDataSetChanged()
        }
    }

    fun addTag(tag: String) {
        tagList.add(tag)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return tagList.size
    }
}