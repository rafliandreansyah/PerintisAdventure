package com.azhara.perintisadventure.ui.home.ui.bookingtour

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.Facilities
import com.azhara.perintisadventure.entity.Tour
import com.azhara.perintisadventure.entity.VisitedTours
import com.azhara.perintisadventure.ui.home.ui.bookingtour.adapter.TourAdapter
import com.azhara.perintisadventure.ui.home.ui.bookingtour.viewmodel.BookingTourViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_detail_booking_tour_success.*
import kotlinx.android.synthetic.main.fragment_list_booking_tour.*
import kotlinx.android.synthetic.main.fragment_list_booking_tour.edt_choose_date_tour
import java.text.SimpleDateFormat
import java.util.*

class ListTourFragment : Fragment(), View.OnClickListener {

    private lateinit var bookingTourViewModel: BookingTourViewModel
    private lateinit var tourAdapter: TourAdapter
    private var DATE: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_booking_tour, container, false)
    }

    override fun onResume() {
        super.onResume()
        bookingTourViewModel.getDataTour()
    }

    override fun onStart() {
        super.onStart()
        loadingShimmer(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edt_choose_date_tour.setOnClickListener(this)
        bookingTourViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BookingTourViewModel::class.java]
        bookingTourViewModel.getDataTour()

        tourAdapter = TourAdapter()
        with(rv_list_tour){
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = tourAdapter
        }

        setData()
        itemClick()
    }

    private fun setData(){
        bookingTourViewModel.dataTour().observe(viewLifecycleOwner, Observer { data ->
            if (data != null){
                Log.d("ListBookingTourFragment", "$data")
                tourAdapter.submitList(data)
                loadingShimmer(false)
            }
        })
    }

    private fun itemClick(){
        tourAdapter.setOnItemClickCallBack(object : TourAdapter.OnItemClickCallBack{
            override fun onClicked(tour: Tour) {
                val date = edt_choose_date_tour.text.toString()
                if (date.isEmpty()){
                    edt_choose_date_tour.error = "Tanggal tidak boleh kosong!!!"
                    context?.let { Toasty.error(it, "Tanggal tidak boleh kosong!", Toast.LENGTH_SHORT, true).show() }
                }
                if (tour != null && (DATE != null || DATE != 0.toLong()) && date.isNotEmpty()){
                    val dataTour = ListTourFragmentDirections
                        .actionListTourFragmentToNavigationDetailDestinationFragment()
                    dataTour.capacity = tour.capacity!!
                    dataTour.dateTour = DATE!!
                    dataTour.durationTour = tour.durationTour!!
                    dataTour.facilities = tour.facilities?.toTypedArray()
                    dataTour.imgUrl = tour.imgUrl!!
                    dataTour.locationTour = tour.locationTour!!
                    dataTour.partnerId = tour.partnerId!!
                    dataTour.price = tour.price!!
                    dataTour.timeTour = tour.timeTour!!
                    dataTour.tourName = tour.tourName!!
                    dataTour.vehicle = tour.vehicle!!
                    dataTour.visitedTour = tour.visitedTour?.toTypedArray()
                    dataTour.tourId = tour.tourId
                    edt_choose_date_tour.text.clear()
                    view?.findNavController()?.navigate(dataTour)
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.edt_choose_date_tour -> {
                chooseDate()
            }
        }
    }

    private fun chooseDate(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker =
            context?.let {
                DatePickerDialog(
                    it,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val dateFormat = SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                        ).format(calendar.time)
                        edt_choose_date_tour.setText(dateFormat)
                        DATE = convertDateToTimeMilis(dateFormat)
                        edt_choose_date_tour.error = null
                    },
                    year,
                    month,
                    date
                )
            }
        datePicker?.datePicker?.minDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000L
        datePicker?.show()
    }
    private fun convertDateToTimeMilis(date: String?): Long {
        val dateTime = "$date"
        val formater = SimpleDateFormat("dd-MM-yyyy")
        val dates = formater.parse(dateTime).time
        return dates
    }

    private fun loadingShimmer(state: Boolean) {
        if (state) {
            shimmer_list_booking_tour.startShimmer()
            shimmer_list_booking_tour.visibility = View.VISIBLE
            rv_list_tour.visibility = View.GONE
        } else {
            shimmer_list_booking_tour.visibility = View.GONE
            rv_list_tour.visibility = View.VISIBLE
            shimmer_list_booking_tour.stopShimmer()
        }
    }
}