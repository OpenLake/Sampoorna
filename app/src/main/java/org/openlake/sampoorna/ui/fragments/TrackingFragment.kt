package org.openlake.sampoorna.ui.fragments


import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialFadeThrough
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.FragmentTrackingBinding
import java.util.*


class TrackingFragment : Fragment(R.layout.fragment_tracking), OnDateSetListener {
    private val menstrualCycle: Int = 28
    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    private lateinit var dateText: TextView

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
        _binding = FragmentTrackingBinding.inflate(inflater,container,false)


        dateText = binding.dateText

        val button: Button = binding.showDialog
        button.setOnClickListener { showDatePickerDialog() }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            this.dateText.text = daysLeft.toString()
        else
            this.dateText.text = "Invalid Date"

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