package com.azhara.perintisadventure.ui.home.ui.bookinglist.bookingdetail.car

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.ui.home.ui.bookinglist.viewmodel.BookingListViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail_booking_car.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DetailBookingCarFragment : Fragment(), View.OnClickListener {

    private lateinit var bookingListViewModel: BookingListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_booking_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingShimmer(true)
        btn_detail_back.setOnClickListener(this)
        bookingListViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BookingListViewModel::class.java]
        val bookingId = DetailBookingCarFragmentArgs.fromBundle(arguments as Bundle).bookingId
        val statusPayment = DetailBookingCarFragmentArgs.fromBundle(arguments as Bundle).statusPayment
        val downPayment = DetailBookingCarFragmentArgs.fromBundle(arguments as Bundle).downPayment
        loadDetailBookingById(bookingId, statusPayment, downPayment)
    }

    private fun loadDetailBookingById(
        bookingId: String?,
        statusPayment: Boolean,
        downPayment: Boolean
    ){
        bookingListViewModel.getDataDetailBookingCar(bookingId)

        bookingListViewModel.dataDetailBookingCar().observe(viewLifecycleOwner, Observer { data ->
            if (data != null){
                val dateStart = data.startDate?.seconds?.let { convertToLocalDate(it) }
                val dateEnd = data.endDate?.seconds?.let { convertToLocalDate(it) }
                tv_detail_driver.text = data.driver
                tv_detail_booking_date.text = "$dateStart - $dateEnd"
                tv_detail_pickup_location.text = data.pickUpArea
                if (statusPayment){
                    tv_status_payment_detail_booking.text = "Pembayaran Berhasil"
                    tv_detail_total_price.text = "Rp. ${data.totalPrice}"
                }
                if (downPayment && !statusPayment){
                    tv_status_payment_detail_booking.text = "Pembayaran DP"
                    activity?.applicationContext?.let {
                        ContextCompat.getColor(
                            it, R.color.colorYellow)
                    }?.let { tv_status_payment_detail_booking.setTextColor(it) }
                    tv_detail_total_price.text = "Rp. ${data.totalPrice?.div(2)}"
                }
//                loadDataPartner(data.partnerId)
                loadDataCar(data.carId)
            }
        })
    }

//    private fun loadDataPartner(partnerId: String?){
//        bookingListViewModel.getPartner(partnerId)
//
//        bookingListViewModel.dataPartner().observe(viewLifecycleOwner, Observer { data ->
//            if (data != null){
//                tv_detail_travel_name.text = data.travelName
//                tv_detail_booking_travel_name_location.text = "Lokasi Kantor ${data.travelName}"
//                tv_detail_booking_travel_location.text = data.address
//            }
//        })
//    }

    private fun convertToLocalDate(date: Long): String {
        // Convert timestamp to local time
        val calendar = Calendar.getInstance()
        val tz = calendar.timeZone
        val sdf = SimpleDateFormat("dd MMMM yyyy hh:mm a")
        sdf.timeZone = tz
        val startSecondDate = Date(date * 1000)
        return sdf.format(startSecondDate)
    }

    private fun loadDataCar(carId: String?){
        bookingListViewModel.getDataCar(carId)

        bookingListViewModel.dataCar().observe(viewLifecycleOwner, Observer { data ->
            if (data != null){
                loadingShimmer(false)
                Glide.with(this).load(data.imgUrl).into(img_detail_booked_car)
                tv_detail_capacity_user.text = "${data.capacity}"
                tv_detail_car_name.text = "${data.carName}"
                    if (data.gear == 0){
                    tv_detail_gear_car.text = "Manual"
                }else{
                    tv_detail_gear_car.text = "Automatic"
                }
                tv_detail_capacity_luggage.text = "${data.luggage}"
                tv_detail_car_year.text = "${data.year}"
                tv_detail_car_plate.text = "${data.carNumberPlate}"
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_detail_back -> {
                activity?.onBackPressed()
            }
        }
    }

    private fun loadingShimmer(state: Boolean) {
        if (state) {
            shimmer_detail_booking_car.startShimmer()
            shimmer_detail_booking_car.visibility = View.VISIBLE
            layout_detail_booking_car.visibility = View.INVISIBLE
        } else {
            shimmer_detail_booking_car.visibility = View.INVISIBLE
            layout_detail_booking_car.visibility = View.VISIBLE
            shimmer_detail_booking_car.stopShimmer()
        }
    }

}
