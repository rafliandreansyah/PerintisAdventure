package com.azhara.perintisadventure.ui.home.ui.bookingcar

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
import com.azhara.perintisadventure.R
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.minibugdev.sheetselection.SheetSelection
import com.minibugdev.sheetselection.SheetSelectionItem
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_detail_booked_car.*
import kotlinx.android.synthetic.main.fragment_detail_ready_car_booking.*
import kotlinx.android.synthetic.main.rv_items_ready_car.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class DetailReadyCarBookingFragment : Fragment(), View.OnClickListener {

    private lateinit var bookingCarViewModel: BookingCarViewModel
    private var PRICE: Long? = null
    private var PRICE_PICKUP: Long? = null

    private var partnerId: String? = null
    private var carId: String? = null
    private var totalPrice: Long? = null
    private var startDate: Long? = null
    private var endDate: Long? = null
    private var driver: String? = null
    private var pickUpArea: String? = null
    private var carName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_ready_car_booking, container, false)

    }

    override fun onStart() {
        super.onStart()
        loadingShimmer(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookingCarViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[BookingCarViewModel::class.java]

        this.startDate = DetailReadyCarBookingFragmentArgs.fromBundle(arguments as Bundle).startDate
        this.endDate = DetailReadyCarBookingFragmentArgs.fromBundle(arguments as Bundle).endDate
        this.driver = DetailReadyCarBookingFragmentArgs.fromBundle(arguments as Bundle).driver
        this.carId = DetailReadyCarBookingFragmentArgs.fromBundle(arguments as Bundle).carId
        this.partnerId = DetailReadyCarBookingFragmentArgs.fromBundle(arguments as Bundle).partnerId
        this.carName = DetailReadyCarBookingFragmentArgs.fromBundle(arguments as Bundle).carName
        val duration = DetailReadyCarBookingFragmentArgs.fromBundle(arguments as Bundle).duration
        val capacity = DetailReadyCarBookingFragmentArgs.fromBundle(arguments as Bundle).capacity
        val carYear = DetailReadyCarBookingFragmentArgs.fromBundle(arguments as Bundle).carYear
        val price = DetailReadyCarBookingFragmentArgs.fromBundle(arguments as Bundle).price
        val gear = DetailReadyCarBookingFragmentArgs.fromBundle(arguments as Bundle).gear
        val imgCar = DetailReadyCarBookingFragmentArgs.fromBundle(arguments as Bundle).imageCar
        val luggage = DetailReadyCarBookingFragmentArgs.fromBundle(arguments as Bundle).luggage

        setData(
            startDate!!,
            endDate!!,
            driver!!,
            duration,
            capacity,
            carYear,
            price,
            gear,
            imgCar,
            partnerId!!,
            luggage,
            carName
        )
        bookingCarViewModel.getDataPickUpArea()
        edt_detail_car_ready_pickup_area.setOnClickListener(this)
        btn_booking_now.setOnClickListener(this)
        radio_office_pickup.setOnClickListener {
            radio_custom_pickup.isChecked = false
            edt_detail_car_ready_pickup_area.isEnabled = false
            edt_detail_car_ready_pickup_detail_area.isEnabled = false
            edt_detail_car_ready_pickup_area.text.clear()
            edt_detail_car_ready_pickup_detail_area.text.clear()
            tv_detail_car_price_area.visibility = View.INVISIBLE
            Toast.makeText(context, radio_office_pickup.text, Toast.LENGTH_SHORT).show()
            this.PRICE_PICKUP = 0
            if (PRICE != null && PRICE_PICKUP != null) {
                this.totalPrice = PRICE!! + PRICE_PICKUP!!
                tv_detail_ready_car_total_price.text = "Rp. $totalPrice"
            }
        }
        radio_custom_pickup.setOnClickListener {
            radio_office_pickup.isChecked = false
            edt_detail_car_ready_pickup_area.isEnabled = true
            edt_detail_car_ready_pickup_detail_area.isEnabled = true
            Toast.makeText(context, radio_custom_pickup.text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setData(
        startDate: Long, endDate: Long, driver: String, duration: Long,
        capacity: Int, carYear: Int, price: Long, gear: Int, imgCar: String,
        partnerId: String, luggage: Int, carName: String?
    ) {
        bookingCarViewModel.getDataPartner(partnerId)

        bookingCarViewModel.dataPartner().observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                loadingShimmer(false)
                context?.let { Glide.with(it).load(imgCar).into(img_detail_ready_car) }
                val startDateFormat = convertToLocalDate(startDate)
                val endDateFormat = convertToLocalDate(endDate)
                tv_detail_ready_car_travel_name.text = data.travelName
                tv_detail_ready_car_booking_date.text = "$startDateFormat - $endDateFormat"
                tv_detail_ready_car_driver.text = driver
                tv_detail_ready_car_capacity.text = "$capacity"
                tv_detail_ready_car_year.text = "$carYear"
                tv_detail_ready_car_location.text = data.address
                tv_location_office_travel.text = "Lokasi kantor ${data.travelName}"
                tv_detail_ready_car_luggage.text = "$luggage"
                activity?.tv_title_toolbar?.text = carName
                this.carName = carName
                this.PRICE = duration * price
                Log.d("data partner", "$data")
                if (gear == 0) {
                    tv_detail_ready_car_gear.text = "Manual"
                } else {
                    tv_detail_ready_car_gear.text = "Automatic"
                }

            } else {
                loadingShimmer(false)
            }
        })

    }

    private fun areaData(): ArrayList<SheetSelectionItem> {
        val areas = ArrayList<SheetSelectionItem>()
        bookingCarViewModel.dataArea().observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                for (area in data) {
                    val sheetSelectionItem = SheetSelectionItem("${area.price}", "${area.area}")
                    areas.add(sheetSelectionItem)
                }
                loadingShimmer(false)
            } else {
                loadingShimmer(false)
            }
        })
        Log.d("Area", "$areas")
        return areas
    }

    private fun chooseArea() {
        context?.let {
            SheetSelection.Builder(it)
                .title("Pilih Area")
                .items(areaData())
                .showDraggedIndicator(true)
                .searchEnabled(false)
                .onItemClickListener { item, _ ->
                    edt_detail_car_ready_pickup_area.setText(item.value)
                    tv_detail_car_price_area.visibility = View.VISIBLE
                    tv_detail_car_price_area.text = "Rp. ${item.key}"
                    this.PRICE_PICKUP = item.key.toLong()
                    totalPrice = PRICE!! + PRICE_PICKUP!!
                    tv_detail_ready_car_total_price.text = "Rp. $totalPrice"
                }
                .show()
        }

    }

    private fun convertToLocalDate(date: Long): String {
        // Convert timestamp to local time
        val calendar = Calendar.getInstance()
        val tz = calendar.timeZone
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a")
        sdf.timeZone = tz
        val startSecondDate = Date(date * 1000)
        return sdf.format(startSecondDate)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edt_detail_car_ready_pickup_area -> {
                chooseArea()
            }
            R.id.btn_booking_now -> {
                btn_booking_now.isEnabled = false
                bookingNow()
            }
        }
    }

    private fun bookingNow() {
        loadingBooking(true)
        if (radio_custom_pickup.isChecked) {
            val areaLocation = edt_detail_car_ready_pickup_area.text.toString()
            val detailLocation = edt_detail_car_ready_pickup_detail_area.text.toString()
            pickUpArea = "$areaLocation, $detailLocation"

            if (detailLocation.isNotEmpty() && areaLocation.isNotEmpty()) {
                if (this.partnerId != null && this.carId != null && this.totalPrice != null
                    && this.startDate != null && this.endDate != null && this.driver != null && this.pickUpArea != null
                    && this.carName != null
                ) {
                    val startDateTimeMilis = this.startDate!! * 1000
                    val endDateTimeMilis = this.endDate!! * 1000

                    bookingCarViewModel.booking(
                        this.partnerId, this.carId, this.totalPrice, Timestamp(Date(startDateTimeMilis))
                        , Timestamp(Date(endDateTimeMilis)), this.driver, this.pickUpArea, this.carName
                    )

                    bookingCarViewModel.checkBooking()
                        .observe(viewLifecycleOwner, Observer { data ->
                            if (data != null) {
                                if (data == true) {
                                    toPayment(totalPrice)
                                } else {
                                    loadingBooking(false)
                                    context?.let {
                                        Toasty.error(
                                            it,
                                            "Gagal booking mobil",
                                            Toast.LENGTH_LONG,
                                            true
                                        ).show()
                                    }
                                }
                                Log.d("status Booking", "$data")
                            }
                        })

                }
            } else if (areaLocation.isEmpty()) {
                loadingBooking(false)
                context?.let {
                    Toasty.error(
                        it,
                        "Anda belum memilih area pengambilan mobil",
                        Toast.LENGTH_LONG,
                        true
                    ).show()
                }
            } else {
                loadingBooking(false)
                context?.let {
                    Toasty.error(
                        it,
                        "Anda belum mengisi detail pengambilan mobil",
                        Toast.LENGTH_LONG,
                        true
                    ).show()
                }
            }
        } else if (radio_office_pickup.isChecked) {
            pickUpArea = "Kantor Rental"
            if (this.partnerId != null && this.carId != null && this.totalPrice != null
                && this.startDate != null && this.endDate != null && this.driver != null && this.pickUpArea != null
                && this.carName != null
            ) {

                val startDateTimeMilis = this.startDate!! * 1000
                val endDateTimeMilis = this.endDate!! * 1000

                bookingCarViewModel.booking(
                    this.partnerId, this.carId, this.totalPrice, Timestamp(Date(startDateTimeMilis))
                    , Timestamp(Date(endDateTimeMilis)), this.driver, this.pickUpArea, this.carName
                )
                bookingCarViewModel.checkBooking().observe(viewLifecycleOwner, Observer { data ->
                    if (data != null) {
                        if (data == true) {
                            toPayment(totalPrice)
                        } else {
                            loadingBooking(false)
                            context?.let {
                                Toasty.error(
                                    it,
                                    "Gagal booking mobil",
                                    Toast.LENGTH_LONG,
                                    true
                                ).show()
                            }
                        }
                        Log.d("status Booking", "$data")
                    }
                })
            }
        } else {
            loadingBooking(false)
            context?.let {
                Toasty.error(
                    it,
                    "Anda belum memilih lokasi pengambilan!",
                    Toast.LENGTH_LONG,
                    true
                ).show()
            }
        }
    }

    private fun toPayment(totalPrice: Long?){
        bookingCarViewModel.bookingListId().observe(viewLifecycleOwner, Observer { id ->
            val toPayment = DetailReadyCarBookingFragmentDirections.actionNavigationDetailReadyCarBookingFragmentToNavigationPayment()
            if (totalPrice != null && id != null){
                loadingBooking(false)
                toPayment.bookingListId = id
                toPayment.totalPrice = totalPrice
                view?.findNavController()?.navigate(toPayment)
            }
        })

    }

    private fun loadingShimmer(state: Boolean) {
        if (state) {
            shimmer_ready_car_detail.startShimmer()
            shimmer_ready_car_detail.visibility = View.VISIBLE
            layout_detail_ready_car.visibility = View.INVISIBLE
        } else {
            shimmer_ready_car_detail.visibility = View.INVISIBLE
            layout_detail_ready_car.visibility = View.VISIBLE
            shimmer_ready_car_detail.stopShimmer()
        }
    }

    private fun loadingBooking(state: Boolean){
        if (state){
            loading_background_booking_car.visibility = View.VISIBLE
            loading_booking_car.visibility = View.VISIBLE
            loading_booking_car.playAnimation()
        }else{
            loading_background_booking_car.visibility = View.INVISIBLE
            loading_booking_car.visibility = View.INVISIBLE
            loading_booking_car.cancelAnimation()
            btn_booking_now.isEnabled = true
        }
    }

}
