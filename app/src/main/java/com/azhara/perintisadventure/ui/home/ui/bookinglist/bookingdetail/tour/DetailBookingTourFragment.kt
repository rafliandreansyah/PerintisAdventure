package com.azhara.perintisadventure.ui.home.ui.bookinglist.bookingdetail.tour

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.azhara.perintisadventure.R

/**
 * A simple [Fragment] subclass.
 */
class DetailBookingTourFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_booking_tour, container, false)
    }

}