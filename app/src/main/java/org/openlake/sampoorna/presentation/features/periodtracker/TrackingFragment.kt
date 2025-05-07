package org.openlake.sampoorna.presentation.features.periodtracker

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var alarmManager: AlarmManager

    // Permission launcher for Android 13+ notification permission
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showDatePickerDialog()
        } else {
            Toast.makeText(
                requireContext(),
                "Notification permission is needed for reminders",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Activity launcher for alarm permissions
    private val alarmPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // Check if we have alarm permission after returning from settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                checkNotificationPermission()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Exact alarm permission is required for period reminders",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

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
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //preparing the OnDateSetListener for the datePickerDialog.
        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
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

            // Set alarm if permissions are granted
            scheduleAlarm(futureDate.time)
        }

        binding.showDialog.setOnClickListener {
            checkAlarmPermission()
        }

        //checking expected
        checkExpectedDate()

        //adding animation
        addAnimation()

        return binding.root
    }

    private fun checkAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // For Android 12 and above, check and request SCHEDULE_EXACT_ALARM
            if (alarmManager.canScheduleExactAlarms()) {
                checkNotificationPermission()
            } else {
                // Show dialog explaining why we need the permission
                Snackbar.make(
                    binding.root,
                    "Exact alarm permission is needed for accurate period predictions",
                    Snackbar.LENGTH_LONG
                ).setAction("Settings") {
                    // Open alarm settings page
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                    alarmPermissionLauncher.launch(intent)
                }.show()
            }
        } else {
            // For older Android versions, direct to date picker
            checkNotificationPermission()
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check notification permission for Android 13+
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    showDatePickerDialog()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Explain why we need notification permission
                    Snackbar.make(
                        binding.root,
                        "Notification permission is needed for period reminders",
                        Snackbar.LENGTH_LONG
                    ).setAction("Grant") {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }.show()
                }
                else -> {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // Notification permission not required for older Android versions
            showDatePickerDialog()
        }
    }

    private fun scheduleAlarm(triggerTimeMillis: Long) {
        try {
            val intent = Intent(requireContext(), PeriodReceiver::class.java)
            intent.putExtra("title", "Hey ${sharedPref.getString(Constants.Name, "")}!!")
            intent.putExtra("content", "Your period is going to begin soon!")

            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                0,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                else
                    PendingIntent.FLAG_UPDATE_CURRENT
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTimeMillis,
                        pendingIntent
                    )
                    Toast.makeText(
                        requireContext(),
                        "Period reminder set successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Use inexact alarm as fallback
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        triggerTimeMillis,
                        pendingIntent
                    )
                    Toast.makeText(
                        requireContext(),
                        "Period reminder set (approximate time)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMillis,
                    pendingIntent
                )
                Toast.makeText(
                    requireContext(),
                    "Period reminder set successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMillis,
                    pendingIntent
                )
                Toast.makeText(
                    requireContext(),
                    "Period reminder set successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: SecurityException) {
            // Handle permission denied case
            Toast.makeText(
                requireContext(),
                "Cannot set period reminder: permission denied",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            // Handle other exceptions
            Toast.makeText(
                requireContext(),
                "Failed to set period reminder: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
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