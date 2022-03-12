package org.openlake.sampoorna.presentation.features.blogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.openlake.sampoorna.R

class BlogAdapter(val context: Context) : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.blog_item,parent,false)
        val viewHolder = BlogViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return 0
    }

    class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val blogImage = itemView.findViewById<ImageView>(R.id.blog_image)
        val blogTitle = itemView.findViewById<TextView>(R.id.blog_text)
        val blogText = itemView.findViewById<TextView>(R.id.blog_text)
    }
}