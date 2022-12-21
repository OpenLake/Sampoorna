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
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.databinding.FragmentSosMessageBottomSheetBinding
import org.openlake.sampoorna.presentation.features.userFeatures.UserViewModel

@AndroidEntryPoint
class SosMessageBottomSheet : BottomSheetDialogFragment() {
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var userViewModel: UserViewModel

    private var _binding : FragmentSosMessageBottomSheetBinding? = null
    private val binding  get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSosMessageBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        sharedPreferences = requireActivity().getSharedPreferences(Constants.Sampoorna,Context.MODE_PRIVATE)

        binding.message.setText(sharedPreferences.getString(Constants.SOSMessage, ""))

        binding.saveSosMessage.setOnClickListener {
            if (!binding.message.text.isNullOrBlank()){
                sharedPreferences.edit()
                    .putString(Constants.SOSMessage, binding.message.text.toString())
                    .apply()
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