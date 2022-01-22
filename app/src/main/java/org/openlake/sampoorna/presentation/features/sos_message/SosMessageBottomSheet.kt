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
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.presentation.features.userFeatures.UserViewModel

@AndroidEntryPoint
class SosMessageBottomSheet : BottomSheetDialogFragment() {
    private lateinit var sosSharedPreferences : SharedPreferences
    private lateinit var viewModel: UserViewModel
    private var sosMessageString = " "
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sos_message_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sosMessage = view.findViewById<TextInputEditText>(R.id.message)
        val saveMessage = view.findViewById<MaterialButton>(R.id.save_sos_message)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        sosSharedPreferences = requireActivity().getSharedPreferences("sosMessage",Context.MODE_PRIVATE)
        saveMessage.setOnClickListener {
            sosMessageString = sosMessage.text.toString()
            if (!sosMessage.text.isNullOrBlank()){
            viewModel.updateSOSMessage(sosMessageString)
            val editor = sosSharedPreferences.edit()
                editor.putString("sosMessage",sosMessageString)
                editor.apply()
                dismiss()
        }
            else{
                Toast.makeText(context, "Please add sos message", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
}