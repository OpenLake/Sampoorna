package org.openlake.sampoorna.presentation.features.blogs

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.data.sources.entities.Blog
import org.openlake.sampoorna.databinding.FragmentCreateBlogBinding

class CreateBlogFragment : Fragment() {

    private var _binding: FragmentCreateBlogBinding? = null
    private val binding: FragmentCreateBlogBinding get() = _binding!!
    private lateinit var blogViewModel: BlogViewModel
    private lateinit var tagListAdapter: BlogTagAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBlogBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences(Constants.Sampoorna, Context.MODE_PRIVATE)

        blogViewModel = ViewModelProvider(this)[BlogViewModel::class.java]

        binding.createBlogScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if(scrollY>oldScrollY) {
                binding.postBlogButton.shrink()
            } else {
                binding.postBlogButton.extend()
            }
        })

        tagListAdapter = BlogTagAdapter(requireContext(), true)
        binding.tagList.adapter = tagListAdapter
        binding.tagList.layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW, FlexWrap.WRAP)

        binding.addTag.setOnClickListener {
            val tag = binding.blogTag.text.toString().trim().lowercase()
            if(tag.isEmpty()) {
                Toast.makeText(requireContext(), "Tag can't be empty", Toast.LENGTH_SHORT).show()
            }
            else if(tagListAdapter.tagList.contains(tag)) {
                Toast.makeText(requireContext(), "Tag has already been added", Toast.LENGTH_SHORT).show()
            }
            else {
                tagListAdapter.addTag(tag)
            }
        }

        binding.postBlogButton.setOnClickListener {
            Log.d("BLOGS", "Post button clicked")

            if(binding.blogTitle.text.isNullOrEmpty() || binding.blogContent.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "One or more required fields are empty", Toast.LENGTH_SHORT).show()
            }
            else {
                Log.d("BLOGS", "Creating blog object")
                val blog = Blog(
                    title = binding.blogTitle.text.toString(),
                    content = binding.blogContent.text.toString(),
                    authorUsername = sharedPreferences.getString(Constants.Username, "") ?: "",
                    authorUid = auth.uid!!,
                    tags = tagListAdapter.tagList,
                    anonymous = binding.anonymousSwitch.isChecked
                )

                Toast.makeText(requireContext(), "Posting blog...", Toast.LENGTH_SHORT).show()

                viewLifecycleOwner.lifecycleScope.launch {
                    Log.d("BLOGS", "Inside coroutine before addBlog")

                    try {
                        val success = blogViewModel.addBlog(blog)
                        Log.d("BLOGS", "addBlog returned: $success")

                        if (!isAdded) {
                            Log.e("BLOGS", "Fragment no longer attached, cannot show UI feedback")
                            return@launch
                        }

                        withContext(Dispatchers.Main) {
                            Log.d("BLOGS", "On Main thread, showing result")
                            if (success) {
                                Toast.makeText(requireActivity(), "Blog posted successfully!", Toast.LENGTH_LONG).show()
                                Log.d("BLOGS", "Toast shown, now navigating back")
                            } else {
                                Toast.makeText(requireActivity(), "Failed to post blog", Toast.LENGTH_LONG).show()
                            }
                        }

                        // Navigate back after feedback
                        withContext(Dispatchers.Main) {
                            delay(1000)
                            Log.d("BLOGS", "Navigating back now")
                        }
                    } catch (e: Exception) {
                        Log.e("BLOGS", "Exception in coroutine: ${e.message}", e)
                        if (isAdded) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireActivity(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                findNavController().popBackStack()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}