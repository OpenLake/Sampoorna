package org.openlake.sampoorna.presentation.features.sos_message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.presentation.features.userFeatures.UserViewModel

@AndroidEntryPoint
class SosMessageBottomSheet : BottomSheetDialogFragment() {
    lateinit var viewModel: UserViewModel
    private var defaultMessage = "Please help I am in danger"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_sos_message_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sosMessage = view.findViewById<EditText>(R.id.message)
        val saveMessage = view.findViewById<Button>(R.id.save_sos_message)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        saveMessage.setOnClickListener {
            val sosMessageString = sosMessage.text.toString()
            if (!sosMessage.text.isNullOrBlank()){
            viewModel.updateSOSMessage(sosMessageString)
                defaultMessage = sosMessageString
        }
            else{
                viewModel.updateSOSMessage(defaultMessage)
            }
        }
    }
}