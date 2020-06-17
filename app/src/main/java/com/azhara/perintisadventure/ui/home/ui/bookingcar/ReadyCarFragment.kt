package com.azhara.perintisadventure.ui.home.ui.bookingcar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.Car
import com.azhara.perintisadventure.ui.home.ui.bookingcar.adapter.CarAdapter
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_ready_car.*
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataStartDate = ReadyCarFragmentArgs.fromBundle(arguments as Bundle).startDate
        val dataEndtDate = ReadyCarFragmentArgs.fromBundle(arguments as Bundle).endDate
        val dataDuration = ReadyCarFragmentArgs.fromBundle(arguments as Bundle).duration
        val dataDriver = ReadyCarFragmentArgs.fromBundle(arguments as Bundle).driver
        Log.d("dataSafeArgs", "$dataStartDate")
        Log.d("dataSafeArgs", "$dataEndtDate")
        Log.d("dataSafeArgs", "$dataDuration")
        Log.d("dataSafeArgs", dataDriver)

        bookingCarViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BookingCarViewModel::class.java]
        bookingCarViewModel.getDataCar()
        dataCarFilter(dataStartDate, dataEndtDate, dataDuration, dataDriver)
        carAdapter = CarAdapter()

        with(rv_ready_car){
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = carAdapter
        }

    }

    private fun dataCarFilter(startDateData: Long?, endDateData: Long?, durationData: Long?, driverData: String?){
        bookingCarViewModel.dataCar().observe(viewLifecycleOwner, Observer { data->
            if (data != null){
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
                carAdapter.submitList(car)
            }
//            Log.d("ReadyCar", "$data")
        })

    }

    private fun convertToLocalDate(){
        // Convert timestamp to local time
//        val calendar = Calendar.getInstance()
//        val tz = calendar.timeZone
//        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a")
//        sdf.timeZone = tz
//        val startSecondDate = startDate?.let { Date(it * 1000) }
//        val date = sdf.format(startSecondDate)
//        Log.d("TimeStamp", "$startDate")
//        Log.d("Date", "$date")
    }


}


