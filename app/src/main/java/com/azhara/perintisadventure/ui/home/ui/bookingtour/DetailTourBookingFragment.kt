package com.azhara.perintisadventure.ui.home.ui.bookingtour

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.ui.home.ui.bookingtour.adapter.FacilityAdapter
import com.azhara.perintisadventure.ui.home.ui.bookingtour.adapter.VisitedTourAdapter
import com.azhara.perintisadventure.ui.home.ui.bookingtour.viewmodel.BookingTourViewModel
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_detail_booking_tour.*
import kotlinx.android.synthetic.main.fragment_detail_ready_car_booking.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DetailTourBookingFragment : Fragment(), View.OnClickListener {

    private lateinit var bookingTourViewModel: BookingTourViewModel
    private var partnerId: String? = null
    private var tourName: String? = null
    private var dateTour: Long? = 0
    private var totalPrice: Long? = 0
    private var tourId: String? = null
    private val bookingType: Int? = 1
    private var randomNumber: Long? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_booking_tour, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingShimmer(true)
        btn_booking_tour_now.setOnClickListener(this)

        randomNumber = randomNumber()?.toLong()

        bookingTourViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BookingTourViewModel::class.java]
        val capacity = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).capacity
        dateTour = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).dateTour
        val durationTour = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).durationTour
        val facilities = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).facilities
        val imgUrl = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).imgUrl
        val locationTour = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).locationTour
        partnerId = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).partnerId
        val price = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).price
        val timeTour = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).timeTour
        tourName = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).tourName
        val vehicle = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).vehicle
        val visitedTour = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).visitedTour
        tourId = DetailTourBookingFragmentArgs.fromBundle(arguments as Bundle).tourId

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

        setData(capacity,
            dateTour!!, durationTour, facilities.toList(), imgUrl, locationTour, partnerId, price, timeTour, tourName, vehicle, visitedTour?.toList())
    }

    private fun setData(capacity: Int?, dateTour: Long, durationTour: String?, facilities: List<String>?
                        , imgUrl: String?, locationTour: String?, partnerId: String?, price: Long?, timeTour: String?
                        , tourName: String?, vehicle: String?, visitedTour: List<String>?){

        tv_capacity_detail_booking_tour.text = "$capacity Orang"
        tv_date_detail_booking_tour.text = "Pemesanan Untuk ${convertLongToTime(dateTour)}"
        tv_duration_detail_booking_tour.text = "$durationTour"
        tv_location_detail_booking_tour.text = "$locationTour"
        tv_time_detail_booking_tour.text = "$timeTour"
        tv_tour_name_detail_booking_tour.text = "$tourName"
        tv_vehicle_detail_booking_tour.text = "$vehicle"
        totalPrice = price!! * capacity?.toLong()!! - randomNumber!!
        tv_total_price_detail_booking_tour.text = "Rp. ${decimalFormat(totalPrice)}"
        context?.let { Glide.with(it).load(imgUrl).into(img_bg_detail_booking_tour) }

        Log.d("list facilities", "$facilities")
        Log.d("list visitedTour", "$visitedTour")
        loadingShimmer(false)
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd MMMM YYYY")
        return format.format(date)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_booking_tour_now -> {
                btn_booking_tour_now.isEnabled = false
                booking()
            }
        }
    }

    private fun booking() {
        loadingBooking(true)
        val detailPickup = edt_pickup_detail_tour.text.toString()

        if (detailPickup.isEmpty()){
            loadingBooking(false)
            context?.let { Toasty.error(it, "Detail penjemputan tidak boleh kosong!", Toast.LENGTH_LONG, true).show() }
        }

        if (detailPickup.length < 8 && detailPickup.isNotEmpty()){
            loadingBooking(false)
            context?.let { Toasty.error(it, "Detail penjemputan kurang lengkap!", Toast.LENGTH_LONG, true).show() }
        }

        if (detailPickup.isNotEmpty() && detailPickup.length >= 8){
            bookingTourViewModel.booking(Timestamp(Date(dateTour!!)), tourId, partnerId, totalPrice, detailPickup, tourName)
//            context?.let { Toasty.success(it, "Pemesanan berhasil!", Toast.LENGTH_LONG, true).show() }
            bookingTourViewModel.checkBooking().observe(viewLifecycleOwner, Observer { data->
                if (data == true){
                    toPayment(totalPrice)
                }else{
                    loadingBooking(false)
                    context?.let { Toasty.error(it, "Terjadi kesalahan!", Toast.LENGTH_LONG, true).show() }
                }
            })
        }
    }

    private fun toPayment(totalPrice: Long?){
        bookingTourViewModel.bookingListId().observe(viewLifecycleOwner, Observer { id ->
            if (id != null){
                val toPayment =
                    DetailTourBookingFragmentDirections.actionNavigationDetailDestinationFragmentToNavigationPayment()
                toPayment.bookingListId = id
                toPayment.totalPrice = totalPrice!!
                toPayment.bookingType = bookingType!!
                view?.findNavController()?.navigate(toPayment)
                loadingBooking(false)
            }
        })
    }

    private fun loadingShimmer(state: Boolean) {
        if (state) {
            shimmer_detail_booking_tour.startShimmer()
            shimmer_detail_booking_tour.visibility = View.VISIBLE
            layout_detail_booking_tour.visibility = View.INVISIBLE
        } else {
            shimmer_detail_booking_tour.visibility = View.INVISIBLE
            layout_detail_booking_tour.visibility = View.VISIBLE
            shimmer_detail_booking_tour.stopShimmer()
        }
    }

    private fun loadingBooking(state: Boolean){
        if (state){
            loading_background_booking_tour.visibility = View.VISIBLE
            loading_booking_tour.visibility = View.VISIBLE
            loading_booking_tour.playAnimation()
        }else{
            loading_background_booking_tour.visibility = View.INVISIBLE
            loading_booking_tour.visibility = View.INVISIBLE
            loading_booking_tour.cancelAnimation()
            btn_booking_tour_now.isEnabled = true
        }
    }

    private fun randomNumber(): Int?{
        return (1..999).random()
    }

    private fun decimalFormat(price: Long?): String?{
        val formatDecimal = DecimalFormat("###,###,###")
        return formatDecimal.format(price)
    }

}
