package com.azhara.perintisadventure.ui.home.ui.bookingtour

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.azhara.perintisadventure.R
import kotlinx.android.synthetic.main.fragment_date_booking_car.*
import kotlinx.android.synthetic.main.fragment_date_booking_tour.*
import kotlinx.android.synthetic.main.fragment_detail_booking_tour.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DateBookingTourFragment : Fragment(), View.OnClickListener {

    private var DATE: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_booking_tour, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_date_booking_tour.setOnClickListener(this)
        edt_choose_date_tour.setOnClickListener(this)


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
                        DATE = dateFormat
                        edt_choose_date_tour.error = null
                    },
                    year,
                    month,
                    date
                )
            }
        datePicker?.datePicker?.minDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L
        datePicker?.show()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_date_booking_tour -> {
                bookingTour()
            }
            R.id.edt_choose_date_tour -> {
                chooseDate()
            }
        }
    }

    private fun bookingTour(){
        val date = edt_choose_date_tour.text.toString()

        if (date.isEmpty()){
            edt_choose_date_tour.error = "Tanggal tidak boleh kosong!!!"
        }

        if (date.isNotEmpty() && DATE != null){

            val toBookingTourList = DateBookingTourFragmentDirections
                .actionNavigationDateBookingTourFragmentToListTourFragment()
            toBookingTourList.dateTour = convertDateToTimeMilis(DATE)
            edt_choose_date_tour.text.clear()
            view?.findNavController()?.navigate(toBookingTourList)
        }
    }

    private fun convertDateToTimeMilis(date: String?): Long {
        val dateTime = "$date"
        val formater = SimpleDateFormat("dd-MM-yyyy")
        val dates = formater.parse(dateTime).time
        return dates
    }

}
