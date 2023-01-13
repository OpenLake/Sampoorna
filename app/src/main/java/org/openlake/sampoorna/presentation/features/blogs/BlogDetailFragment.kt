package org.openlake.sampoorna.presentation.features.blogs

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.databinding.FragmentBlogDetailBinding
import org.openlake.sampoorna.presentation.features.blogs.comments.CommentAdapter
import org.openlake.sampoorna.presentation.features.blogs.comments.CommentBottomSheetFragment
import java.util.*

class BlogDetailFragment : Fragment() {

    private var _binding: FragmentBlogDetailBinding? = null
    private val binding: FragmentBlogDetailBinding get() = _binding!!
    private val args: BlogDetailFragmentArgs by navArgs()
    private lateinit var blogViewModel: BlogViewModel
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var menuProvider: MenuProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlogDetailBinding.inflate(inflater, container, false)

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(m: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.blog_detail_menu, m)
                blogViewModel.savedBlogIds.observe(viewLifecycleOwner) { savedBlogs ->
                    if(args.blogId in savedBlogs) {
                        m.findItem(R.id.save_blog).isVisible = false
                        m.findItem(R.id.not_save_blog).isVisible = true
                    }
                    else {
                        m.findItem(R.id.save_blog).isVisible = true
                        m.findItem(R.id.not_save_blog).isVisible = false
                    }
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    R.id.save_blog -> {
                        blogViewModel.saveBlog(args.blogId)
                    }
                    R.id.not_save_blog -> {
                        blogViewModel.deleteSavedBlog(args.blogId)
                    }
                }
                return true
            }
        }

        requireActivity().addMenuProvider(menuProvider)

        blogViewModel = ViewModelProvider(this)[BlogViewModel::class.java]
        blogViewModel.getBlog(args.blogId)

        blogViewModel.blog.observe(viewLifecycleOwner) {blog ->
            binding.loadingAnim.visibility = View.GONE
            binding.blogContentLayout.visibility = View.VISIBLE

            binding.blogTitle.text = blog.title
            binding.blogContent.text = blog.content
            binding.blogTags.layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)

            if(!blog.anonymous) {
                binding.blogAuthor.setOnClickListener {
                    it.findNavController().navigate(R.id.profileFragment, bundleOf("uid" to blog.authorUid))
                }

                blogViewModel.getUser(blog.authorUid).observe(viewLifecycleOwner) {
                    Glide.with(requireContext())
                        .load(it.photoUrl)
                        .placeholder(R.drawable.womenlogo)
                        .centerCrop()
                        .into(binding.blogAuthorImg)
                }
            }

            val tagAdapter = BlogTagAdapter(requireContext())
            tagAdapter.tagList = blog.tags.toMutableList()
            binding.blogTags.adapter = tagAdapter
            binding.blogAuthor.text = "By ${if(blog.anonymous) "Anonymous" else blog.authorUsername}"

            val blogDate = Date(blog.timestamp)

            val hours = blogDate.hours
            val mins = blogDate.minutes
            val time = "${if(hours > 9) "" else "0"}${hours}:${if(mins > 9) "" else "0"}${mins}"
            val date = "${blogDate.date} ${Constants.months[blogDate.month]} ${blogDate.year + 1900}"
            binding.blogTime.text = "$time, $date"
        }

        blogViewModel.getSavedBlogIds()

        binding.blogContentLayout.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if(scrollY>oldScrollY) {
                binding.commentFab.shrink()
            } else {
                binding.commentFab.extend()
            }
        })

        binding.commentFab.setOnClickListener {
            val commentFragment = CommentBottomSheetFragment(blogViewModel, args.blogId, binding.blogAuthor.text.toString().replace("By ", ""))
            commentFragment.show(parentFragmentManager, null)
        }

        commentAdapter = CommentAdapter(requireContext(), blogViewModel, viewLifecycleOwner, parentFragmentManager)
        binding.commentList.adapter = commentAdapter

        blogViewModel.getComments(args.blogId).observe(viewLifecycleOwner) {
            commentAdapter.commentList = it
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        requireActivity().removeMenuProvider(menuProvider)
        super.onDestroyView()
    }
}