package org.openlake.sampoorna.presentation.features.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        blogAdapter = BlogAdapter(requireContext())
        binding.blogList.adapter = blogAdapter

        blogViewModel.getBlogs()
        blogViewModel.blogList.observe(viewLifecycleOwner) {
            blogAdapter.setBlogs(it)
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

    fun showNoInternet() {
        binding.blogLayout.visibility = View.GONE
        binding.noInternetLayout.visibility = View.VISIBLE
    }

    fun hideNoInternet() {
        binding.blogLayout.visibility = View.VISIBLE
        binding.noInternetLayout.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        blogAdapter = BlogAdapter(requireContext())
        blogList.adapter = blogAdapter
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}