package org.openlake.sampoorna.presentation.features.blogs.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.openlake.sampoorna.databinding.FragmentCommentBottomSheetBinding
import org.openlake.sampoorna.presentation.features.blogs.BlogViewModel

class CommentBottomSheetFragment(blogViewModel: BlogViewModel, reply: Boolean = false): BottomSheetDialogFragment() {

    private var _binding: FragmentCommentBottomSheetBinding? = null
    private val binding: FragmentCommentBottomSheetBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentBottomSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}