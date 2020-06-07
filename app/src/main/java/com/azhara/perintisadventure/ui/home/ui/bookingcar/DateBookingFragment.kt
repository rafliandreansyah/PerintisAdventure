package com.azhara.perintisadventure.ui.home.ui.bookingcar

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker

import com.azhara.perintisadventure.R
import com.minibugdev.sheetselection.SheetSelection
import com.minibugdev.sheetselection.SheetSelectionItem
import kotlinx.android.synthetic.main.fragment_date_booking.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DateBookingFragment : Fragment(), View.OnClickListener{

    private val calendar = Calendar.getInstance()
    private var DATE: String? = null
    private var DURATION: String? = null
    private var TIME: String? = null
    private var DRIVER: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edt_choose_date_car.setOnClickListener(this)
        edt_choose_duration_car.setOnClickListener(this)
        edt_choose_time_car.setOnClickListener(this)
        edt_choose_driver_car.setOnClickListener(this)
        btn_cari_mobil.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.edt_choose_date_car ->{
                chooseDate()
            }
            R.id.edt_choose_duration_car ->{
                chooseDuration()
            }
            R.id.edt_choose_time_car ->{
                chooseTime()
            }
            R.id.edt_choose_driver_car ->{
                chooseDriver()
            }
            R.id.btn_cari_mobil -> {

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
                DatePickerDialog(it,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
                    edt_choose_date_car.setText(dateFormat)
                    DATE = dateFormat
                    Log.d("DateBooking", DATE)
                }, year, month, date)
            }
        datePicker?.show()

    }

    private fun chooseDuration(){
        val items = listOf(
            SheetSelectionItem("1", "1 Hari", R.drawable.ic_queue),
            SheetSelectionItem("2", "2 Hari", R.drawable.ic_queue),
            SheetSelectionItem("3", "3 Hari", R.drawable.ic_queue),
            SheetSelectionItem("4", "4 Hari", R.drawable.ic_queue),
            SheetSelectionItem("5", "5 Hari", R.drawable.ic_queue),
            SheetSelectionItem("6", "6 Hari", R.drawable.ic_queue),
            SheetSelectionItem("7", "7 Hari", R.drawable.ic_queue)
        )

        context?.let {
            SheetSelection.Builder(it)
                .title("Pilih Durasi")
                .items(items)
                .showDraggedIndicator(true)
                .searchEnabled(false)
                .onItemClickListener { item, _ ->
                    edt_choose_duration_car.setText(item.value)
                    DURATION = item.value
                    Log.d("DateBooking", DURATION)
                }
                .show()
        }
    }

    private fun chooseTime(){
        val items = listOf(
            SheetSelectionItem("0", "00.00", R.drawable.ic_clock_black),
            SheetSelectionItem("1", "01.00", R.drawable.ic_clock_black),
            SheetSelectionItem("2", "02.00", R.drawable.ic_clock_black),
            SheetSelectionItem("3", "03.00", R.drawable.ic_clock_black),
            SheetSelectionItem("4", "04.00", R.drawable.ic_clock_black),
            SheetSelectionItem("5", "05.00", R.drawable.ic_clock_black),
            SheetSelectionItem("6", "06.00", R.drawable.ic_clock_black),
            SheetSelectionItem("7", "07.00", R.drawable.ic_clock_black),
            SheetSelectionItem("8", "08.00", R.drawable.ic_clock_black),
            SheetSelectionItem("9", "09.00", R.drawable.ic_clock_black),
            SheetSelectionItem("10", "10.00", R.drawable.ic_clock_black),
            SheetSelectionItem("11", "11.00", R.drawable.ic_clock_black),
            SheetSelectionItem("12", "12.00", R.drawable.ic_clock_black),
            SheetSelectionItem("13", "13.00", R.drawable.ic_clock_black),
            SheetSelectionItem("14", "14.00", R.drawable.ic_clock_black),
            SheetSelectionItem("15", "15.00", R.drawable.ic_clock_black),
            SheetSelectionItem("16", "16.00", R.drawable.ic_clock_black),
            SheetSelectionItem("17", "17.00", R.drawable.ic_clock_black),
            SheetSelectionItem("18", "18.00", R.drawable.ic_clock_black),
            SheetSelectionItem("19", "19.00", R.drawable.ic_clock_black),
            SheetSelectionItem("20", "20.00", R.drawable.ic_clock_black),
            SheetSelectionItem("21", "21.00", R.drawable.ic_clock_black),
            SheetSelectionItem("22", "22.00", R.drawable.ic_clock_black),
            SheetSelectionItem("23", "23.00", R.drawable.ic_clock_black)
        )

        context?.let {
            SheetSelection.Builder(it)
                .title("Pilih Jam")
                .items(items)
                .showDraggedIndicator(true)
                .searchEnabled(false)
                .onItemClickListener { item, position ->
                    edt_choose_time_car.setText(item.value)
                    TIME = item.value
                    Log.d("DateBooking", TIME)
                }
                .show()
        }
    }

    private fun chooseDriver(){
        val items = listOf(
            SheetSelectionItem("1", "Tanpa Sopir", R.drawable.ic_steering_wheel),
            SheetSelectionItem("2", "Dengan Sopir", R.drawable.ic_driver)
        )

        context?.let {
            SheetSelection.Builder(it)
                .title("Pilih Sopir")
                .items(items)
                .showDraggedIndicator(true)
                .searchEnabled(false)
                .onItemClickListener { item, position ->
                    edt_choose_driver_car.setText(item.value)
                    DRIVER = item.value
                    Log.d("DateBooking", DRIVER)
                }
                .show()
        }
    }

}
