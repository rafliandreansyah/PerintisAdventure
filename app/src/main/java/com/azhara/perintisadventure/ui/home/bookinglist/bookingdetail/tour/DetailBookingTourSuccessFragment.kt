package com.azhara.perintisadventure.ui.home.bookinglist.bookingdetail.tour

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.ui.home.bookinglist.viewmodel.BookingListViewModel
import com.azhara.perintisadventure.ui.home.bookingtour.adapter.FacilityAdapter
import com.azhara.perintisadventure.ui.home.bookingtour.adapter.VisitedTourAdapter
import kotlinx.android.synthetic.main.fragment_detail_booking_tour_success.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DetailBookingTourSuccessFragment : Fragment(), View.OnClickListener {

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
        btn_detail_maps_booking_tour_success.setOnClickListener(this)
        btn_to_back_booking_tour_success.setOnClickListener(this)
        loadingShimmer(true)
        bookingListViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[BookingListViewModel::class.java]
        val bookingId =
            DetailBookingTourSuccessFragmentArgs.fromBundle(arguments as Bundle).bookingId
        setData(bookingId)
    }

    private fun setData(bookingId: String?) {
        bookingListViewModel.getDetailBookingTour(bookingId)

        bookingListViewModel.dataDetailBookingTour().observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                tv_date_detail_booking_tour_success.text = data.date?.seconds?.let {
                    convertToLocalDate(
                        it
                    )
                }
                tv_total_price_detail_tour_success.text = "Rp. ${decimalFormat(data.totalPrice)}"
                tv_detail_booking_tour_pickup_success.text = "${data.pickupArea}"
                getDataTour("${data.tourId}")
            }
        })
    }

    private fun getDataTour(tourId: String?) {
        bookingListViewModel.getDataTour(tourId)

        bookingListViewModel.dataTour().observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                loadingShimmer(false)
                tv_title_detail_tour_success.text = "${data.tourName}"
                tv_location_detail_tour_success.text = "${data.locationTour}"
                tv_time_detail_tour_success.text = "${data.timeTour}"
                tv_duration_tour_detail_success.text = "${data.durationTour}"
                tv_capacity_detail_tour_success.text = "${data.capacity} Orang"
                tv_vehicle_detail_tour_success.text = "${data.vehicle}"
                val visitedTourAdapter = data.visitedTour?.let { VisitedTourAdapter(it) }
                with(rv_detail_visited_tour_success) {
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = visitedTourAdapter
                }
                val facilityAdapter = data.facilities?.let { FacilityAdapter(it) }
                with(rv_detail_facility_tour_success) {
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = facilityAdapter
                }
            }
        })
    }

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

    private fun decimalFormat(price: Long?): String? {
        val formatDecimal = DecimalFormat("###,###,###")
        return formatDecimal.format(price)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_to_back_booking_tour_success -> {
                activity?.onBackPressed()
            }
            R.id.btn_detail_maps_booking_tour_success -> {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_detail_booking_tour_success_fragment_to_mapsFragment)
            }
        }
    }

}
