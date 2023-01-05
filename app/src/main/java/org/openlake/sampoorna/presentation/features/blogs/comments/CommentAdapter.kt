package org.openlake.sampoorna.presentation.features.blogs.comments

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.data.sources.entities.Comment
import org.openlake.sampoorna.presentation.features.blogs.BlogViewModel

class CommentAdapter(
    val context: Context,
    private val blogViewModel: BlogViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val fragmentManager: FragmentManager
): Adapter<CommentAdapter.CommentViewHolder>() {

    var commentList: MutableList<Comment> = mutableListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    inner class CommentViewHolder(val itemView: View): ViewHolder(itemView) {
        val authorImage: ImageView = itemView.findViewById(R.id.author_image)
        val authorUsername: TextView = itemView.findViewById(R.id.author_username)
        val commentTime: TextView = itemView.findViewById(R.id.comment_time)
        val commentContent: TextView = itemView.findViewById(R.id.comment_content)
        val showReply: TextView = itemView.findViewById(R.id.show_reply_button)
        val replyButton: TextView = itemView.findViewById(R.id.reply_button)
        val replyList: RecyclerView = itemView.findViewById(R.id.reply_list)
        lateinit var replyAdapter: CommentAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentList[position]

        holder.authorUsername.text = if(comment.anonymous) "Anonymous" else comment.authorUsername
        holder.commentTime.text = Constants.getDateString(comment.timestamp)
        holder.commentContent.text = comment.content

        holder.showReply.setOnClickListener {
            if(holder.replyList.visibility == View.VISIBLE) {
                holder.replyList.visibility = View.GONE
                holder.showReply.text = "Show ${holder.replyAdapter.itemCount} replies"
            }
            else if(holder.replyAdapter.itemCount > 0) {
                holder.replyList.visibility = View.VISIBLE
                holder.showReply.text = "Hide replies"
            }
        }

        holder.replyButton.setOnClickListener {
            val commentSheetFragment = CommentBottomSheetFragment(blogViewModel, comment.commentId, if(comment.anonymous) "Anonymous" else comment.authorUsername, true)
            commentSheetFragment.show(fragmentManager, null)
        }

        holder.replyAdapter = CommentAdapter(context, blogViewModel, viewLifecycleOwner, fragmentManager)
        holder.replyList.adapter = holder.replyAdapter

        blogViewModel.getComments(comment.commentId).observe(viewLifecycleOwner) {
            holder.replyAdapter.commentList = it
            if(holder.replyList.visibility == View.GONE) {
                holder.showReply.text = "Show ${holder.replyAdapter.itemCount} replies"
            }
        }

    }

    override fun getItemCount(): Int {
        return commentList.size
    }

}