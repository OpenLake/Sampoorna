package org.openlake.sampoorna.presentation.features.blogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import org.openlake.sampoorna.databinding.FragmentCreateBlogBinding

class CreateBlogFragment : Fragment() {

    private var _binding: FragmentCreateBlogBinding? = null
    private val binding: FragmentCreateBlogBinding get() = _binding!!
    private lateinit var blogViewModel: BlogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBlogBinding.inflate(inflater, container, false)

        blogViewModel = ViewModelProvider(this)[BlogViewModel::class.java]

        binding.createBlogScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if(scrollY>oldScrollY) {
                binding.postBlogButton.shrink()
            } else {
                binding.postBlogButton.extend()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}