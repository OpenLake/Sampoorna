package org.openlake.sampoorna.presentation.features.periodtracker

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialFadeThrough
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.databinding.FragmentTrackingBinding
import org.openlake.sampoorna.util.services.PeriodReceiver
import java.util.*

class TrackingFragment : Fragment() {
    private val menstrualCycle = 28

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: SharedPreferences
    private lateinit var calender: Calendar
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTrackingBinding.inflate(inflater, container, false)

        sharedPref = requireActivity().getSharedPreferences(Constants.Sampoorna, Context.MODE_PRIVATE)
        calender = Calendar.getInstance()

        //preparing the OnDateSetListener for the datePickerDialog.
        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            //Selected Time
            val selectedTime = Calendar.getInstance()
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
            sharedPref.edit()
                .putLong("expectedDate", futureDate.time)
                .apply()

            val intent = Intent(requireContext(), PeriodReceiver::class.java)
            intent.putExtra("title", "Hey ${sharedPref.getString(Constants.Name,"")}!!")
            intent.putExtra("content", "Your period is going to begin soon!")
            val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_UPDATE_CURRENT)
            val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if(Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, futureDate.time + 5000, pendingIntent)
            }
            else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureDate.time, pendingIntent)
            }

        }

        binding.showDialog.setOnClickListener { showDatePickerDialog() }

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
            binding.dateText.text = getString(R.string.enter_mensuration_last_date)
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


    @SuppressLint("SetTextI18n")
    private fun showDaysLeft(daysLeft: Long) {

        if (daysLeft > 0)
            binding.dateText.text = "$daysLeft ${getString(R.string.days_left)}"
        else {
            binding.dateText.text = getString(R.string.enter_valid_date)
        }
    }

    private fun calculateDaysLeft(futureDate: Long): Long {

        // Current Time and Date
        val calendar = Calendar.getInstance()
        val today = calendar.time

        // Calculating left days
        val timeLeft = futureDate - today.time
        val secondsLeft = timeLeft / 1000
        val minutesLeft = secondsLeft / 60
        val hoursLeft = minutesLeft / 60

        return hoursLeft / 24
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            dateSetListener,
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        //constrained the datePickerDialog such that the user can select a date only before the current date.
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}

