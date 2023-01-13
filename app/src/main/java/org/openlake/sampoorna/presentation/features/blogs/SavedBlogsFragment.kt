package org.openlake.sampoorna.presentation.features.blogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.FragmentSavedBlogsBinding

class SavedBlogsFragment : Fragment() {

    private var _binding: FragmentSavedBlogsBinding? = null
    private val binding: FragmentSavedBlogsBinding get() = _binding!!
    private lateinit var blogViewModel: BlogViewModel
    private lateinit var blogAdapter: BlogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBlogsBinding.inflate(inflater, container, false)

        blogViewModel = ViewModelProvider(this)[BlogViewModel::class.java]
        blogViewModel.getSavedBlogIds()
        blogViewModel.savedBlogIds.observe(viewLifecycleOwner) {
            blogViewModel.getSavedBlogs(it)
            if(it.isEmpty()) {
                binding.animLayout.visibility = View.VISIBLE
            }
            else {
                binding.animLayout.visibility = View.INVISIBLE
            }
        }

        blogAdapter = BlogAdapter(requireContext(), blogViewModel, viewLifecycleOwner)
        binding.savedBlogsList.adapter = blogAdapter

        blogViewModel.savedBlogs.observe(viewLifecycleOwner) {
            blogAdapter.blogList = it.toMutableList()
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}