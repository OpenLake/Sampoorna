package org.openlake.sampoorna.presentation.features.blogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.openlake.sampoorna.databinding.FragmentBlogsBinding


class BlogsFragment : Fragment() {

    private lateinit var binding : FragmentBlogsBinding
    private lateinit var blogList : RecyclerView
    private lateinit var blogAdapter: BlogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBlogsBinding.inflate(inflater,container,false)
        blogList = binding.blogList
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        blogAdapter = BlogAdapter(requireContext())
        blogList.adapter = blogAdapter
    }

}