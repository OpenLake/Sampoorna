package org.openlake.sampoorna.presentation.features.blogs

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.openlake.sampoorna.databinding.FragmentFilterBottomSheetBinding

class FilterBottomSheetFragment(private val blogViewModel: BlogViewModel): BottomSheetDialogFragment() {

    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding: FragmentFilterBottomSheetBinding get() = _binding!!
    private lateinit var tagAdapter: BlogTagAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("lol", "on create view called")
        _binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)

        tagAdapter = BlogTagAdapter(requireContext(), true)
        binding.tagList.layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW, FlexWrap.WRAP)
        binding.tagList.adapter = tagAdapter
        blogViewModel.filterTags.observe(viewLifecycleOwner) {
            tagAdapter.tagList = it.toMutableList()
        }

        binding.addTag.setOnClickListener {
            val tag = binding.filterTag.text.toString().trim().lowercase()
            if(tag.isEmpty()) {
                Toast.makeText(requireContext(), "Tag can't be empty", Toast.LENGTH_SHORT).show()
            }
            else {
                tagAdapter.addTag(tag)
            }
        }

        binding.applyFilter.setOnClickListener {
            blogViewModel.filterTags.postValue(tagAdapter.tagList)
            dismiss()
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}