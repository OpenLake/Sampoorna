package org.openlake.sampoorna.presentation.features.periodtracker


import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialFadeThrough
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.FragmentTrackingBinding
import java.util.*
import android.graphics.drawable.AnimationDrawable
import android.widget.CalendarView

class TrackingFragment : Fragment(R.layout.fragment_tracking){
    private val menstrualCycle: Int = 28
    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    private lateinit var dateText: TextView
    private lateinit var sharedPref: SharedPreferences
    lateinit var calender:Calendar
    lateinit var dateSetListener:DatePickerDialog.OnDateSetListener
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
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        dateText = binding.dateText
        calender= Calendar.getInstance()

        //preparing the OnDateSetListener for the datePickerDialog.
        dateSetListener=DatePickerDialog.OnDateSetListener { view, year,month, dayOfMonth ->
            //Selected Time
        val selectedTime: Calendar = Calendar.getInstance()
        selectedTime.set(year, month, dayOfMonth)

        // adding menstrualCycle to selected time
        selectedTime.add(Calendar.DATE, menstrualCycle)

        // Expected future date
        val futureDate = selectedTime.time

        // Calculating left days
        val daysLeft = calculateDaysLeft(futureDate.time)

        //showing the results
        showDaysLeft(daysLeft)

        //saving the result
        sharedPref.edit()?.putLong("expectedDate", futureDate.time)?.apply()

        }
        
        val button: Button = binding.showDialog
        button.setOnClickListener { showDatePickerDialog() }

        //checking expected
        checkExpectedDate()

        //adding animation
        addAnimation()

        return binding.root
    }

    private fun checkExpectedDate() {
        if (sharedPref.contains("expectedDate")) {
            val futureDate = sharedPref.getLong("expectedDate", 0)
            // Calculating left days
            val daysLeft = calculateDaysLeft(futureDate)
            showDaysLeft(daysLeft)
        } else {
            dateText.text = "Please enter the last date of \nthe menstrual period"
        }
    }

    private fun addAnimation() {
        val constraintLayout: ConstraintLayout = binding.layout
        val animationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun showDaysLeft(daysLeft: Long) {

        if (daysLeft > 0)
            dateText.text = "$daysLeft Days Left";
        else {
            this.dateText.text = "Please enter a valid Date"
        }

    }

    private fun calculateDaysLeft(futureDate: Long): Long {

        // Current Time and Date
        val calendar: Calendar = Calendar.getInstance()
        val today: Date = calendar.time

        // Calculating left days
        val timeLeft = futureDate - today.time
        val secondsLeft = timeLeft / 1000
        val minutesLeft = secondsLeft / 60
        val hoursLeft = minutesLeft / 60
        val daysLeft = hoursLeft / 24

        return daysLeft
    }

    private fun showDatePickerDialog() {
       val datepickerDialog=DatePickerDialog(
           requireContext(),
           dateSetListener,
           calender.get(Calendar.YEAR),
           calender.get(Calendar.MONTH),
           calender.get(Calendar.DAY_OF_MONTH)
       )
        //constrained the datePickerDialog such that the user can select a date only before the current date.
        datepickerDialog.datePicker.maxDate=System.currentTimeMillis()
        datepickerDialog.show()

    }
}

