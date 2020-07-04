package com.azhara.perintisadventure.ui.home.ui.bookingtour

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.azhara.perintisadventure.R

/**
 * A simple [Fragment] subclass.
 */
class DetailTourBookingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_booking_tour_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val capacity = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).capacity
        val dateTour = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).dateTour
        val durationTour = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).durationTour
        val facilities = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).facilities
        val imgUrl = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).imgUrl
        val locationTour = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).locationTour
        val partnerId = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).partnerId
        val price = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).price
        val timeTour = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).timeTour
        val tourName = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).tourName
        val vehicle = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).vehicle
        val visitedTour = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).visitedTour

    }

}
