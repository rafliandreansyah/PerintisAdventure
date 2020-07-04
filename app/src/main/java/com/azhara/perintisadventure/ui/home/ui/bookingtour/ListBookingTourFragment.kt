package com.azhara.perintisadventure.ui.home.ui.bookingtour

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.ui.home.ui.bookingtour.adapter.TourAdapter
import com.azhara.perintisadventure.ui.home.ui.bookingtour.viewmodel.BookingTourViewModel
import kotlinx.android.synthetic.main.fragment_list_booking_tour.*

class ListTourFragment : Fragment() {

    private lateinit var bookingTourViewModel: BookingTourViewModel
    private lateinit var tourAdapter: TourAdapter

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

        bookingTourViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BookingTourViewModel::class.java]
        bookingTourViewModel.getDataTour()

        tourAdapter = TourAdapter()
        with(rv_list_tour){
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = tourAdapter
        }

        setData()
    }

    private fun setData(){
        bookingTourViewModel.dataTour().observe(viewLifecycleOwner, Observer { data ->
            if (data != null){
                Log.d("ListBookingTourFragment", "$data")
                tourAdapter.submitList(data)
            }
        })

    }
}