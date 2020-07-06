package com.azhara.perintisadventure.ui.home.ui.bookingtour

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
import kotlinx.android.synthetic.main.fragment_list_booking_tour.*

class ListTourFragment : Fragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = ListTourFragmentArgs.fromBundle(arguments as Bundle).dateTour
        if (date != null || date != 0.toLong()){
            DATE = date
        }
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
            }
        })
    }

    private fun itemClick(){
        tourAdapter.setOnItemClickCallBack(object : TourAdapter.OnItemClickCallBack{
            override fun onClicked(tour: Tour) {
                if (tour != null && (DATE != null || DATE != 0.toLong())){
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

                    view?.findNavController()?.navigate(dataTour)
                }else{
                    activity?.let { Toasty.error(it, "Terajadi kesalahan!", Toast.LENGTH_LONG, true).show() }
                }
            }
        })
    }
}