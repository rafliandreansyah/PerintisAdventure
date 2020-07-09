package com.azhara.perintisadventure.ui.home.ui.bookinglist.bookingdetail.tour

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.ui.home.ui.bookinglist.viewmodel.BookingListViewModel
import com.azhara.perintisadventure.ui.home.ui.bookingtour.adapter.FacilityAdapter
import com.azhara.perintisadventure.ui.home.ui.bookingtour.adapter.VisitedTourAdapter
import kotlinx.android.synthetic.main.fragment_detail_booking_tour.*
import kotlinx.android.synthetic.main.fragment_detail_booking_tour_success.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DetailBookingTourSuccessFragment : Fragment() {

    private lateinit var bookingListViewModel: BookingListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_booking_tour_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingShimmer(true)
        bookingListViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BookingListViewModel::class.java]
        val bookingId = DetailBookingTourSuccessFragmentArgs.fromBundle(arguments as Bundle).bookingId
        setData(bookingId)
    }

    private fun setData(bookingId: String?) {
        bookingListViewModel.getDetailBookingTour(bookingId)

        bookingListViewModel.dataDetailBookingTour().observe(viewLifecycleOwner, Observer { data ->
            if (data != null){
                tv_date_detail_booking_tour_success.text = data.date?.seconds?.let {
                    convertToLocalDate(
                        it
                    )
                }
                tv_total_price_detail_tour_success.text = "${data.totalPrice}"
                tv_detail_booking_tour_pickup_success.text = "${data.pickupArea}"
                getDataTour("${data.tourId}", "${data.partnerId}")
            }
        })
    }

    private fun getDataTour(tourId: String?, partnerId: String?){
        bookingListViewModel.getDataTour(tourId)

        bookingListViewModel.dataTour().observe(viewLifecycleOwner, Observer { data ->
            if (data != null){
                loadingShimmer(false)
                tv_title_detail_tour_success.text = "${data.tourName}"
                tv_location_detail_tour_success.text = "${data.locationTour}"
                tv_time_detail_tour_success.text = "${data.timeTour}"
                tv_duration_tour_detail_success.text = "${data.durationTour}"
                tv_capacity_detail_tour_success.text = "${data.capacity} Orang"
                tv_vehicle_detail_tour_success.text = "${data.vehicle}"
                val visitedTourAdapter = data.visitedTour?.let { VisitedTourAdapter(it) }
                with(rv_detail_visited_tour_success){
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = visitedTourAdapter
                }
                val facilityAdapter = data.facilities?.let { FacilityAdapter(it) }
                with(rv_detail_facility_tour_success){
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = facilityAdapter
                }
//                setDataPartner("${data.partnerId}")
            }
        })
    }

//    private fun setDataPartner(partnerId: String?){
//        bookingListViewModel.getPartner(partnerId)
//
//        bookingListViewModel.dataPartner().observe(viewLifecycleOwner, Observer { data ->
//            if (data != null){
//                loadingShimmer(false)
//                tv_travel_name_detail_tour_success.text = "${data.travelName}"
//                tv_title_name_travel_detail_tour_success.text = "Lokasi Kantor ${data.travelName}"
//                tv_location_travel_detail_tour_success.text = "${data.address}"
//            }
//        })
//    }

    private fun convertToLocalDate(date: Long): String {
        // Convert timestamp to local time
        val calendar = Calendar.getInstance()
        val tz = calendar.timeZone
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        sdf.timeZone = tz
        val startSecondDate = Date(date * 1000)
        return sdf.format(startSecondDate)
    }

    private fun loadingShimmer(state: Boolean) {
        if (state) {
            shimmer_detail_booking_tour_success.startShimmer()
            shimmer_detail_booking_tour_success.visibility = View.VISIBLE
            layout_detail_booking_tour_success.visibility = View.INVISIBLE
        } else {
            shimmer_detail_booking_tour_success.visibility = View.INVISIBLE
            layout_detail_booking_tour_success.visibility = View.VISIBLE
            shimmer_detail_booking_tour_success.stopShimmer()
        }
    }

}
