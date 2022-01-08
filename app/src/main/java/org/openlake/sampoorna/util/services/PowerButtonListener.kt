package org.openlake.sampoorna.util.services

import android.hardware.SensorManager

class PowerButtonListener(private var listener:OnDoubleClickListener) {
    private var mListener: OnDoubleClickListener? = null
    private var mClickCount = 0
    fun setOnClickListener(listener: OnDoubleClickListener?) {
        mListener = listener
    }

    interface OnDoubleClickListener {
        fun onClick(count: Int)
    }
}