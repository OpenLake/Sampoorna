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
import java.util.*


import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.transition.MaterialFadeThrough


class TrackingFragment : Fragment(R.layout.fragment_tracking), OnDateSetListener {
    lateinit var dateText: TextView
    private val menstrualCycle: Int = 28


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //val binding = FragmentTrackingBinding.inflate(inflater,container,false)

        //val view = binding.root
        val cl = inflater.inflate(R.layout.fragment_tracking, container, false) as ConstraintLayout

        dateText = cl.findViewById(R.id.date_text)


        val button = cl.findViewById<View>(R.id.show_dialog)
        button?.setOnClickListener { showDatePickerDialog() }

        return cl
    }


    override fun onDateSet(view: DatePicker, year: Int, month: Int, date: Int) {

        // Current Time and Date
        val calendar: Calendar = Calendar.getInstance()
        val today: Date = calendar.time

        // Selected Time
        val selectedTime: Calendar = Calendar.getInstance()
        selectedTime.set(year, month, date)

        // adding menstrualCycle to selected time
        selectedTime.add(Calendar.DATE, menstrualCycle)

        // Expected future date
        val futureDate = selectedTime.time

        // Calculating left days
        val timeLeft = futureDate.time - today.time
        val secondsLeft = timeLeft / 1000
        val minutesLeft = secondsLeft / 60
        val hoursLeft = minutesLeft / 60
        val daysLeft = hoursLeft / 24

        if (timeLeft > 0)
            this.dateText!!.text = daysLeft.toString()
        else
            this.dateText!!.text = "Invalid Date"

    }

    private fun showDatePickerDialog() {

        context?.let {
            DatePickerDialog(
                it,
                this,
                Calendar.getInstance()[Calendar.YEAR],
                Calendar.getInstance()[Calendar.MONTH],
                Calendar.getInstance()[Calendar.DAY_OF_MONTH]
            )
        }?.show()
    }
}
