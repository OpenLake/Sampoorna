package org.openlake.sampoorna.ui.fragments

import org.openlake.sampoorna.R
import android.view.LayoutInflater
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.openlake.sampoorna.databinding.FragmentTrackingBinding
import java.util.*

class TrackingFragment : Fragment(R.layout.fragment_tracking), OnDateSetListener {
    private var dateText: TextView? = null
    //private var context: Context? = null
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_tracking)
        dateText = findViewById(R.id.date_text)
        findViewById<View>(R.id.show_dialog).setOnClickListener { showDatePickerDialog() }
    }*/

    private fun showDatePickerDialog() {
        val datePickerDialog = this.context?.let {
            DatePickerDialog(
                it,
                this,
                Calendar.getInstance()[Calendar.YEAR],
                Calendar.getInstance()[Calendar.MONTH],
                Calendar.getInstance()[Calendar.DAY_OF_MONTH]
            )
        }
        if (datePickerDialog != null) {
            datePickerDialog.show()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentTrackingBinding.inflate(inflater,container,false)
        val view = binding.root
        dateText = getView()?.findViewById(R.id.date_text)
        getView()?.findViewById<View>(R.id.show_dialog)?.setOnClickListener { showDatePickerDialog() }
        return view
    }


    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        //val calendar = Calendar.getInstance()
        //System.out.println("Current Calendar's Day: " + calendar.get(Calendar.DATE));


        //int finaldate=dayOfMonth+28-dayy;
        val date = "month/day/year: " + (month + 1) + "/" + dayOfMonth + "/" + year
        this.dateText!!.text = date
    }
}
