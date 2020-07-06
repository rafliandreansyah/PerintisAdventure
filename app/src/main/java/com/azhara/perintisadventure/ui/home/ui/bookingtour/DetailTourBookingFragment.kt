package com.azhara.perintisadventure.ui.home.ui.bookingtour

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.ui.home.ui.bookingtour.adapter.FacilityAdapter
import com.azhara.perintisadventure.ui.home.ui.bookingtour.adapter.VisitedTourAdapter
import com.azhara.perintisadventure.ui.home.ui.bookingtour.viewmodel.BookingTourViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail_booking_tour.*

/**
 * A simple [Fragment] subclass.
 */
class DetailTourBookingFragment : Fragment() {

    private lateinit var bookingTourViewModel: BookingTourViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_booking_tour, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookingTourViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BookingTourViewModel::class.java]
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

        val facilityListAdapter = facilities?.toList()?.let { FacilityAdapter(it) }!!
        with(rv_list_facility){
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = facilityListAdapter
            isNestedScrollingEnabled = false
        }
        val visitedTourAdapter = visitedTour?.toList()?.let { VisitedTourAdapter(it) }
        with(rv_list_visited_tour){
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = visitedTourAdapter
            isNestedScrollingEnabled = false
        }

        setData(capacity, dateTour, durationTour, facilities.toList(), imgUrl, locationTour, partnerId, price, timeTour, tourName, vehicle, visitedTour?.toList())
    }

    private fun setData(capacity: Int?, dateTour: Long, durationTour: String?, facilities: List<String>?
                        , imgUrl: String?, locationTour: String?, partnerId: String?, price: Long?, timeTour: String?
                        , tourName: String?, vehicle: String?, visitedTour: List<String>?){

        tv_capacity_detail_booking_tour.text = "$capacity Orang"
        tv_date_detail_booking_tour.text = "$dateTour"
        tv_duration_detail_booking_tour.text = "$durationTour"
        tv_location_detail_booking_tour.text = "$locationTour"
        tv_time_detail_booking_tour.text = "$timeTour"
        tv_tour_name_detail_booking_tour.text = "$tourName"
        tv_vehicle_detail_booking_tour.text = "$vehicle"
        val totalPrice = price!! * capacity?.toLong()!!
        tv_total_price_detail_booking_tour.text = "$totalPrice"
        context?.let { Glide.with(it).load(imgUrl).into(img_bg_detail_booking_tour) }

        Log.d("list facilities", "$facilities")
        Log.d("list visitedTour", "$visitedTour")
    }

}
