package org.openlake.sampoorna.presentation.features.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.databinding.FragmentBlogDetailBinding
import java.util.*

class BlogDetailFragment : Fragment() {

    private var _binding: FragmentBlogDetailBinding? = null
    private val binding: FragmentBlogDetailBinding get() = _binding!!
    private val args: BlogDetailFragmentArgs by navArgs()
    private lateinit var blogViewModel: BlogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlogDetailBinding.inflate(inflater, container, false)

        blogViewModel = ViewModelProvider(this)[BlogViewModel::class.java]
        blogViewModel.getBlog(args.blogId)

        blogViewModel.blog.observe(viewLifecycleOwner) {blog ->
            binding.loadingAnim.visibility = View.GONE
            binding.blogContentLayout.visibility = View.VISIBLE

            binding.blogTitle.text = blog.title
            binding.blogContent.text = blog.content
            binding.blogTags.layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)

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

        binding.blogContentLayout.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if(scrollY>oldScrollY) {
                binding.commentFab.shrink()
            } else {
                binding.commentFab.extend()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}