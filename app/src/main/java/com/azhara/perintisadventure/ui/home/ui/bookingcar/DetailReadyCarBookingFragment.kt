package com.azhara.perintisadventure.ui.home.ui.bookingcar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.azhara.perintisadventure.R

/**
 * A simple [Fragment] subclass.
 */
class DetailReadyCarBookingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_ready_car_booking, container, false)
    }

}
