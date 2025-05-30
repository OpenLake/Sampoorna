package org.openlake.sampoorna.presentation.features.blogs

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.data.sources.entities.Blog
import java.util.*

class BlogAdapter(val context: Context, private val blogViewModel: BlogViewModel, private val viewLifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    var blogList: MutableList<Blog> = mutableListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.blog_item, parent, false)
        return BlogViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val blog = blogList[position]

        holder.blogTitle.text = blog.title
        holder.blogContent.text = blog.content
        holder.blogTags.layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)

        val tagAdapter = BlogTagAdapter(context)
        tagAdapter.tagList = blog.tags.toMutableList()
        holder.blogTags.adapter = tagAdapter
        holder.blogAuthor.text = "By ${if(blog.anonymous) "Anonymous" else blog.authorUsername}"

        if(!blog.anonymous) {
            //added  null check to avoid crashes
            blogViewModel.getUser(blog.authorUid).observe(viewLifecycleOwner) { user ->
                user?.let {
                    Glide.with(context)
                        .load(it.photoUrl ?: "")
                        .placeholder(R.drawable.womenlogo)
                        .centerCrop()
                        .into(holder.blogAuthorImage)
                }
            }

        }

        val blogDate = Date(blog.timestamp)

        val hours = blogDate.hours
        val mins = blogDate.minutes
        val time = "${if(hours > 9) "" else "0"}${hours}:${if(mins > 9) "" else "0"}${mins}"
        val date = "${blogDate.date} ${Constants.months[blogDate.month]} ${blogDate.year + 1900}"
        holder.blogTime.text = "$time, $date"

        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.blogDetailFragment, bundleOf("blogId" to blog.blogId))
        }
    }

    override fun getItemCount(): Int {
        return blogList.size
    }

    class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val blogTitle: TextView = itemView.findViewById(R.id.blog_title)
        val blogContent: TextView = itemView.findViewById(R.id.blog_content)
        val blogTags: RecyclerView = itemView.findViewById(R.id.blog_tags)
        val blogAuthor: TextView = itemView.findViewById(R.id.blog_author)
        val blogAuthorImage: ImageView = itemView.findViewById(R.id.blog_author_img)
        val blogTime: TextView = itemView.findViewById(R.id.blog_time)
    }
}