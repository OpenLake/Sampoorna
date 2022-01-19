package org.openlake.sampoorna.presentation.features.periodtracker


import android.annotation.SuppressLint
import android.app.*
import android.app.DatePickerDialog.OnDateSetListener

import android.content.SharedPreferences
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
import android.graphics.drawable.AnimationDrawable
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import android.content.Context
import android.text.format.DateFormat


class TrackingFragment : Fragment(R.layout.fragment_tracking), OnDateSetListener {
    private val menstrualCycle: Int = 28
    private var time: Long=0
    private var futureDate1:  Date = TODO()
    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    private lateinit var dateText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
        createNotificationChannel()
        binding.buttonNotif.setOnClickListener { scheduleNotification()}
    }

    @SuppressLint("ServiceCast")
    private fun scheduleNotification() {

        val intent = Intent(activity, Notifications01::class.java)
        startActivity(intent)
        val title = "Hi, Girl!"
        val message = "2 days left, get ready"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            activity,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager =
            context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        alarmManager?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

    }

    private fun showAlert(time: Long, title: String, message: String)
    {
        AlertDialog.Builder(activity)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + futureDate1)
            .setPositiveButton("Okay"){_,_ ->}
            .show()
    }

    private fun getTime(view: DatePicker, year: Int, month: Int, date: Int){

        val calendar1: Calendar = Calendar.getInstance()
        val today: Date = calendar1.time

        val selectedTime1: Calendar = Calendar.getInstance()
        selectedTime1.set(year, month, date)

        val timeLeft1 = today.time
        val secondsLeft1 = timeLeft1 / 1000
        val minutesLeft1 = secondsLeft1 / 60
        val hoursLeft1 = minutesLeft1 / 60
        val daysLeft1 = hoursLeft1 / 24

        val calendar = Calendar.getInstance()
        calendar.set(year, month, date, hoursLeft1.toInt(), minutesLeft1.toInt())
        time= calendar.timeInMillis

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

        //adding animation
        val constraintLayout: ConstraintLayout = binding.layout
        val animationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

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

        //saving the result

        var sharedPref : SharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)

        sharedPref?.edit()?.putString("bla", daysLeft.toString())?.apply()

        val result=sharedPref.getString("bla", "")


        if (timeLeft > 0)
            dateText.setText(result);
            //this.dateText.text = sharedPref.toString()
        else
            this.dateText.text = "Invalid Date"

        //calculating date for notifying
         selectedTime.add(Calendar.DATE, -2)
         futureDate1 = selectedTime.time


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
    private fun createNotificationChannel()
    {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}