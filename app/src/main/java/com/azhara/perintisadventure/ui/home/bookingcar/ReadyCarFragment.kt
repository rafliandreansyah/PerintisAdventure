package com.azhara.perintisadventure.ui.home.bookingcar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.Car
import com.azhara.perintisadventure.ui.home.bookingcar.adapter.CarAdapter
import com.azhara.perintisadventure.ui.home.bookingcar.viewmodel.BookingCarViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_ready_car.*

/**
 * A simple [Fragment] subclass.
 */
class ReadyCarFragment : Fragment() {

    private lateinit var bookingCarViewModel: BookingCarViewModel
    private lateinit var carAdapter: CarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ready_car, container, false)
    }

    override fun onStart() {
        super.onStart()
        loadingShimmer(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.tv_title_toolbar?.text = "Ready Car"
        val dataStartDate = ReadyCarFragmentArgs.fromBundle(arguments as Bundle).startDate
        val dataEndtDate = ReadyCarFragmentArgs.fromBundle(arguments as Bundle).endDate
        val dataDuration = ReadyCarFragmentArgs.fromBundle(arguments as Bundle).duration
        val dataDriver = ReadyCarFragmentArgs.fromBundle(arguments as Bundle).driver
        Log.d("dataSafeArgs", "$dataStartDate")
        Log.d("dataSafeArgs", "$dataEndtDate")
        Log.d("dataSafeArgs", "$dataDuration")
        Log.d("dataSafeArgs", dataDriver)

        bookingCarViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[BookingCarViewModel::class.java]
        bookingCarViewModel.getDataCar()
        dataCarFilter(dataStartDate, dataEndtDate, dataDuration, dataDriver)
        carAdapter = CarAdapter()
        with(rv_ready_car) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = carAdapter
        }
    }

    private fun dataCarFilter(
        startDateData: Long?,
        endDateData: Long?,
        durationData: Long?,
        driverData: String?
    ) {
        bookingCarViewModel.dataCar().observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                val car = data.filter { car ->
                    (car.booked?.all {
                        (it.startDate?.seconds != startDateData && it.endDate?.seconds != endDateData) &&
                                it.endDate?.seconds != startDateData &&
                                it.startDate?.seconds != endDateData &&
                                !(it.startDate?.seconds!! < startDateData!! && it.endDate?.seconds!! > endDateData!!) &&
                                !(it.startDate?.seconds!! > startDateData && it.startDate?.seconds!! < endDateData!!) &&
                                !(it.endDate?.seconds!! > startDateData && it.endDate?.seconds!! < endDateData!!) &&
                                !(it.startDate?.seconds!! > startDateData && it.endDate?.seconds!! < endDateData!!)
                    }!!)

                }
                car.forEach { data ->
                    Log.d("Date from car", "${data.booked}")
                    Log.d("Date from startuser", "$startDateData")
                    Log.d("Date from enduser", "$endDateData")
                }
                Log.d("ReadyCar Car", "$car")
                if (car.isNotEmpty()) {
                    animBookingCarEmpty(false)
                }
                if (car.isEmpty()) {
                    animBookingCarEmpty(true)
                }
                carAdapter.submitList(car)
                progressBooking(startDateData, endDateData, durationData, driverData)
                loadingShimmer(false)
            }
        })

    }

    private fun progressBooking(
        startDateData: Long?,
        endDateData: Long?,
        durationData: Long?,
        driverData: String?
    ) {
        carAdapter.setOnItemClickCallBack(object : CarAdapter.OnItemClickCallBack {
            override fun onItemClick(car: Car?) {
                val detailCar = ReadyCarFragmentDirections
                    .actionNavigationReadyCarFragmentToNavigationDetailCarBookingFragment()
                if (startDateData != null && endDateData != null && driverData != null && durationData != null) {
                    detailCar.startDate = startDateData
                    detailCar.endDate = endDateData
                    detailCar.driver = driverData
                    detailCar.duration = durationData
                }
                detailCar.capacity = car?.capacity!!
                detailCar.carYear = car.year!!
                detailCar.price = car.price!!
                detailCar.gear = car.gear!!
                detailCar.imageCar = car.imgUrl!!
                detailCar.partnerId = car.partnerId!!
                detailCar.carId = car.carId!!
                detailCar.carName = car.carName!!
                detailCar.luggage = car.luggage!!
                detailCar.carNumberPlates = car.carNumberPlate!!

                if (car.carId != null) {
                    view?.findNavController()?.navigate(detailCar)
                }
            }

        })
    }

    private fun loadingShimmer(state: Boolean) {
        if (state) {
            shimmer_ready_car.startShimmer()
            shimmer_ready_car.visibility = View.VISIBLE
            rv_ready_car.visibility = View.INVISIBLE
        } else {
            shimmer_ready_car.visibility = View.INVISIBLE
            rv_ready_car.visibility = View.VISIBLE
            shimmer_ready_car.stopShimmer()
        }
    }

    private fun animBookingCarEmpty(state: Boolean) {
        if (state) {
            ilustration_empty.visibility = View.VISIBLE
            tv_empty_car.visibility = View.VISIBLE
        } else {
            ilustration_empty.visibility = View.INVISIBLE
            tv_empty_car.visibility = View.INVISIBLE
        }
    }
}


