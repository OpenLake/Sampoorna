package org.openlake.sampoorna.presentation.features.sos_message

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.FragmentSosMessageBottomSheetBinding
import org.openlake.sampoorna.presentation.features.userFeatures.UserViewModel

@AndroidEntryPoint
class SosMessageBottomSheet : BottomSheetDialogFragment() {
    private lateinit var sosSharedPreferences : SharedPreferences
    private lateinit var viewModel: UserViewModel
    private var _binding : FragmentSosMessageBottomSheetBinding? = null
    private val binding  get() = _binding!!
    private var sosMessageString = " "
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSosMessageBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        sosSharedPreferences = requireActivity().getSharedPreferences("sosMessage",Context.MODE_PRIVATE)

        viewModel.userDetails.observe(viewLifecycleOwner){
            val user = it[0]
            binding.message.setText(user.sosMessage)
            binding.message.setSelection(binding.message.text.toString().length)
        }

        binding.saveSosMessage.setOnClickListener {
            sosMessageString = binding.message.text.toString()
            if (!binding.message.text.isNullOrBlank()){
                viewModel.updateSOSMessage(sosMessageString)
                val editor = sosSharedPreferences.edit()
                editor.putString("sosMessage",sosMessageString)
                editor.apply()
                dismiss()
            }
            else{
                Toast.makeText(context, getString(R.string.please_enter_sos_message), Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}