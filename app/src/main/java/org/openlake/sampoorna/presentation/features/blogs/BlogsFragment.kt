package org.openlake.sampoorna.presentation.features.blogs

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.openlake.sampoorna.App
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.FragmentBlogsBinding


class BlogsFragment : Fragment() {

    private var _binding: FragmentBlogsBinding? = null
    private val binding : FragmentBlogsBinding get() = _binding!!
    private lateinit var blogList : RecyclerView
    private lateinit var blogAdapter: BlogAdapter
    private lateinit var blogViewModel: BlogViewModel
    private lateinit var menuProvider: MenuProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlogsBinding.inflate(inflater, container, false)
        blogList = binding.blogList

        blogViewModel = ViewModelProvider(this)[BlogViewModel::class.java]

        if(!App.isOnline(requireActivity())) {
            showNoInternet()
        }

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if(App.isOnline(requireActivity())) {
                    menuInflater.inflate(R.menu.blogs_menu, menu)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    R.id.saved_blogs -> {
                        findNavController().navigate(R.id.savedBlogsFragment)
                    }
                }
                return true
            }

        }

        requireActivity().addMenuProvider(menuProvider)

        blogAdapter = BlogAdapter(requireContext(), blogViewModel, viewLifecycleOwner)
        binding.blogList.adapter = blogAdapter

        blogViewModel.getBlogs()

        blogViewModel.searchResults.observe(viewLifecycleOwner) {
            blogAdapter.blogList = it.toMutableList()
            if(it.isEmpty()) {
                if(blogViewModel.searchQuery.value.isNullOrEmpty() && blogViewModel.filterTags.value.isNullOrEmpty()) {
                    showSearching()
                }
                else {
                    showNoResult()
                }
            }
            else {
                hideAnimations()
            }
        }

        binding.blogSearch.doOnTextChanged { text, start, before, count ->
            blogViewModel.searchQuery.postValue(text.toString())
        }

        blogViewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            showSearching()
            blogViewModel.blogList.observe(viewLifecycleOwner) { blogs ->
                blogViewModel.filterTags.observe(viewLifecycleOwner) {tags ->
                    blogViewModel.searchResults.postValue(blogs.filter { blog ->
                        (blog.content.lowercase().contains(query.lowercase()) || blog.title.lowercase().contains(query.lowercase())) && (tags.isEmpty() || tags.any { it in blog.tags })
                    }.toMutableList())
                }
            }
        }

        blogViewModel.filterTags.observe(viewLifecycleOwner) {
            if(it.isEmpty()) {
                binding.filterCountCard.visibility = View.GONE
            }
            else {
                binding.filterCountCard.visibility = View.VISIBLE
                binding.filterCount.text = it.size.toString()
            }
        }

        binding.blogFilter.setOnClickListener {
            val filterFragment = FilterBottomSheetFragment(blogViewModel)
            filterFragment.show(parentFragmentManager, null)
        }

        binding.noInternetAnim.imageAssetsFolder = "images"

        binding.retryButton.setOnClickListener {
            if(App.isOnline(requireActivity())) {
                hideNoInternet()
            }
            else {
                Toast.makeText(requireContext(), "Couldn't connect to the internet", Toast.LENGTH_SHORT).show()
            }
        }

        binding.addBlog.setOnClickListener {
            findNavController().navigate(R.id.createBlogFragment)
        }

        return binding.root
    }

    private fun showNoInternet() {
        binding.blogLayout.visibility = View.GONE
        binding.noInternetLayout.visibility = View.VISIBLE
    }

    private fun hideNoInternet() {
        binding.blogLayout.visibility = View.VISIBLE
        binding.noInternetLayout.visibility = View.GONE
    }

    private fun showSearching() {
        binding.animation.setAnimation(R.raw.search)
        binding.animText.text = "Searching..."

        binding.animLayout.visibility = View.VISIBLE
        binding.blogList.visibility = View.GONE
    }

    private fun showNoResult() {
        binding.animation.setAnimation(R.raw.emptybox)
        binding.animText.text = "No results found :("

        binding.animLayout.visibility = View.VISIBLE
        binding.blogList.visibility = View.GONE
    }

    private fun hideAnimations() {
        binding.animLayout.visibility = View.GONE
        binding.blogList.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        blogAdapter = BlogAdapter(requireContext(), blogViewModel, viewLifecycleOwner)
        blogList.adapter = blogAdapter
    }

    override fun onDestroyView() {
        _binding = null
        requireActivity().removeMenuProvider(menuProvider)
        super.onDestroyView()
    }
}