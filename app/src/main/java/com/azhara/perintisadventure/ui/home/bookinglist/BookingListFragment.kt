package com.azhara.perintisadventure.ui.home.bookinglist

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
import com.azhara.perintisadventure.entity.BookingList
import com.azhara.perintisadventure.ui.home.bookinglist.adapter.BookingListAdapter
import com.azhara.perintisadventure.ui.home.bookinglist.viewmodel.BookingListViewModel
import kotlinx.android.synthetic.main.fragment_booking_list.*

class BookingListFragment : Fragment() {

    private lateinit var bookingListViewModel: BookingListViewModel
    private lateinit var bookingListAdapter: BookingListAdapter

    override fun onStart() {
        super.onStart()
        getDataList()
    }

    override fun onResume() {
        super.onResume()
        getDataList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_booking_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookingListViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[BookingListViewModel::class.java]
        bookingListAdapter = BookingListAdapter()
        with(rv_booking_list) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = bookingListAdapter
        }
        dataBookingList()
    }

    private fun getDataList() {
        bookingListViewModel.getDataBookingList()
    }

    private fun dataBookingList() {
        bookingListViewModel.dataBookingList().observe(viewLifecycleOwner, Observer { data ->
            Log.d("Booking data", "$data")
            if (data != null) {
                animBookingEmpty(false)
                bookingListAdapter.submitList(data)
                onItemClick()
            }
            if (data.isEmpty()) {
                animBookingEmpty(true)
            }
        })
    }

    private fun onItemClick() {
        bookingListAdapter.setOnItemClickCallBack(object : BookingListAdapter.OnItemClickCallBack {
            override fun onItemClicked(item: BookingList?) {
                if (item?.statusPayment == false) {
                    val toPayment = BookingListFragmentDirections
                        .actionNavigationBookedToNavigationPayment()
                    toPayment.imgUrlProofPayment = "${item.imgUrlProofPayment}"
                    toPayment.totalPrice = item.totalPrice!!
                    toPayment.uploadProofPayemnt = item.uploadProofPayment!!
                    toPayment.bookingListId = item.bookingListId!!
                    toPayment.bookingType = item.bookingType!!
                    view?.findNavController()?.navigate(toPayment)
                }
                if (item?.statusPayment == true && item.bookingId != null && item.bookingType == 0) {
                    val toDetailBookingCar = BookingListFragmentDirections
                        .actionNavigationBookedToDetailBookingCarFragment()
                    toDetailBookingCar.bookingId = "${item.bookingId}"
                    view?.findNavController()?.navigate(toDetailBookingCar)
                }
                if (item?.statusPayment == true && item.bookingId != null && item.bookingType == 1) {
                    val toDetailBookingTour = BookingListFragmentDirections
                        .actionNavigationBookedToDetailBookingTourSuccessFragment()
                    toDetailBookingTour.bookingId = item.bookingId
                    view?.findNavController()?.navigate(toDetailBookingTour)
                }

            }

        })
    }

    private fun animBookingEmpty(state: Boolean) {
        if (state) {
            anim_booking_empty.playAnimation()
            layout_booking.visibility = View.VISIBLE
        } else {
            layout_booking.visibility = View.INVISIBLE
            anim_booking_empty.cancelAnimation()
        }
    }
}
