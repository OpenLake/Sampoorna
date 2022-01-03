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


import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout


class TrackingFragment : Fragment(R.layout.fragment_tracking), OnDateSetListener,
    View.OnClickListener {
    private var dateText: TextView? = null


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
        //val binding = FragmentTrackingBinding.inflate(inflater,container,false)

        //val view = binding.root
        val cl = inflater.inflate(R.layout.fragment_tracking, container, false) as ConstraintLayout

        dateText = cl.findViewById(R.id.date_text)


        val button = cl.findViewById<View>(R.id.show_dialog)
        button?.setOnClickListener(this)

        return cl
    }


    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        val nowDate = calendar.get(Calendar.DATE)
        val nowMonth = calendar.get(Calendar.MONTH)+1;
        var temp = month+1
        var daysleft = 0
        var f = 0


        var finaldate = dayOfMonth + 28
        if (month == 2 || month == 4 || month == 6 || month == 9 || month == 11) {
            if (finaldate > 30) {
                finaldate = finaldate - 30

            }
        }
        else {
            if (finaldate > 31) {
                finaldate = finaldate - 31

            }
        }



          if(nowMonth==1 && temp==12 && finaldate>=nowDate) {
              System.out.println("heyy")
              f = 1;
              daysleft = finaldate - nowDate
          }
          else if(nowMonth==temp &&  dayOfMonth<=nowDate){
              f = 1;
              daysleft = finaldate - nowDate
          }

          else if(temp<nowMonth && finaldate>=nowDate){
              f = 1;
              daysleft = finaldate - nowDate
          }





            if(f==1)

            this.dateText!!.text = daysleft.toString()
            else 

                this.dateText!!.text = "Invalid"

    }

    override fun onClick(p0: View?) {

        showDatePickerDialog()
    }
}
