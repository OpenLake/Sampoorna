package org.openlake.sampoorna.util.services

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class PowerButtonListener {
    private var mListener: OnDoubleClickListener? = null
    private var mClickCount = 0
    fun setOnShakeListener(listener: OnDoubleClickListener?) {
        mListener = listener
    }

    public interface OnDoubleClickListener {
        fun onClick(count: Int)
    }
}