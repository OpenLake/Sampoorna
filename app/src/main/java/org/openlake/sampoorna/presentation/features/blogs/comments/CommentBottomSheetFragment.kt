package org.openlake.sampoorna.presentation.features.blogs.comments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.data.sources.entities.Comment
import org.openlake.sampoorna.databinding.FragmentCommentBottomSheetBinding
import org.openlake.sampoorna.presentation.features.blogs.BlogViewModel

class CommentBottomSheetFragment(private val blogViewModel: BlogViewModel, private val postId: String, val reply: Boolean = false): BottomSheetDialogFragment() {

    private var _binding: FragmentCommentBottomSheetBinding? = null
    private val binding: FragmentCommentBottomSheetBinding get() = _binding!!
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentBottomSheetBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences(Constants.Sampoorna, Context.MODE_PRIVATE)

        binding.postComment.setOnClickListener {
            val commentContent = binding.commentContent.text.toString()
            if(commentContent.isEmpty()) {
                Toast.makeText(requireContext(), "Comment can't be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                binding.postComment.isEnabled = false
                val comment = Comment(
                    authorUid = auth.uid!!,
                    authorUsername = sharedPreferences.getString(Constants.Username, "")!!,
                    content = commentContent,
                    anonymous = binding.anonymousSwitch.isChecked,
                    commentedOnId = postId
                )
                blogViewModel.addComment(comment) {
                    binding.postComment.isEnabled = true
                    if(it.isSuccessful) {
                        dismiss()
                    }
                    else {
                        Toast.makeText(requireContext(), it.exception?.message, Toast.LENGTH_SHORT).show()
                        it.exception?.printStackTrace()
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}