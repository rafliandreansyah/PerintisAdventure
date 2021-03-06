package com.azhara.perintisadventure.ui.home.bookingcar

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.azhara.perintisadventure.R
import com.minibugdev.sheetselection.SheetSelection
import com.minibugdev.sheetselection.SheetSelectionItem
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_date_booking_car.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DateBookingFragment : Fragment(), View.OnClickListener {

    private var DATE: String? = null
    private var DURATION: Long? = null
    private var TIME: String? = null
    private var DRIVER: String? = null
    private var STARTDATE: Long? = null
    private var ENDDATE: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_booking_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edt_choose_date_car.setOnClickListener(this)
        edt_choose_duration_car.setOnClickListener(this)
        edt_choose_time_car.setOnClickListener(this)
        edt_choose_driver_car.setOnClickListener(this)
        btn_cari_mobil.setOnClickListener(this)

        activity?.tv_title_toolbar?.text = "Informasi Pemesanan"
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edt_choose_date_car -> {
                chooseDate()
            }
            R.id.edt_choose_duration_car -> {
                chooseDuration()
            }
            R.id.edt_choose_time_car -> {
                chooseTime()
            }
            R.id.edt_choose_driver_car -> {
                chooseDriver()
            }
            R.id.btn_cari_mobil -> {
                cariMobil()
            }
        }
    }

    private fun chooseDate() {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker =
            context?.let {
                DatePickerDialog(
                    it,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val dateFormat = SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                        ).format(calendar.time)
                        edt_choose_date_car.setText(dateFormat)
                        DATE = dateFormat
                        edt_choose_date_car.error = null
                    },
                    year,
                    month,
                    date
                )
            }
        datePicker?.datePicker?.minDate = System.currentTimeMillis() + 1 * 24 * 60 * 60 * 1000L
        datePicker?.show()

    }

    private fun chooseDuration() {
        val items = listOf(
            SheetSelectionItem("1", "1 Hari", R.drawable.ic_queue),
            SheetSelectionItem("2", "2 Hari", R.drawable.ic_queue),
            SheetSelectionItem("3", "3 Hari", R.drawable.ic_queue),
            SheetSelectionItem("4", "4 Hari", R.drawable.ic_queue),
            SheetSelectionItem("5", "5 Hari", R.drawable.ic_queue),
            SheetSelectionItem("6", "6 Hari", R.drawable.ic_queue),
            SheetSelectionItem("7", "7 Hari", R.drawable.ic_queue),
            SheetSelectionItem("8", "8 Hari", R.drawable.ic_queue),
            SheetSelectionItem("9", "9 Hari", R.drawable.ic_queue),
            SheetSelectionItem("10", "10 Hari", R.drawable.ic_queue),
            SheetSelectionItem("11", "11 Hari", R.drawable.ic_queue),
            SheetSelectionItem("12", "12 Hari", R.drawable.ic_queue),
            SheetSelectionItem("13", "13 Hari", R.drawable.ic_queue),
            SheetSelectionItem("14", "14 Hari", R.drawable.ic_queue)
        )

        context?.let {
            SheetSelection.Builder(it)
                .title("Pilih Durasi")
                .items(items)
                .showDraggedIndicator(true)
                .searchEnabled(false)
                .onItemClickListener { item, _ ->
                    edt_choose_duration_car.setText(item.value)
                    DURATION = item.key.toLong()
                    edt_choose_duration_car.error = null
                }
                .show()
        }
    }

    private fun chooseTime() {
        val items = listOf(
            SheetSelectionItem("7", "07:00", R.drawable.ic_clock_black),
            SheetSelectionItem("8", "08:00", R.drawable.ic_clock_black),
            SheetSelectionItem("9", "09:00", R.drawable.ic_clock_black),
            SheetSelectionItem("10", "10:00", R.drawable.ic_clock_black),
            SheetSelectionItem("11", "11:00", R.drawable.ic_clock_black),
            SheetSelectionItem("12", "12:00", R.drawable.ic_clock_black),
            SheetSelectionItem("13", "13:00", R.drawable.ic_clock_black),
            SheetSelectionItem("14", "14:00", R.drawable.ic_clock_black),
            SheetSelectionItem("15", "15:00", R.drawable.ic_clock_black),
            SheetSelectionItem("16", "16:00", R.drawable.ic_clock_black),
            SheetSelectionItem("17", "17:00", R.drawable.ic_clock_black),
            SheetSelectionItem("18", "18:00", R.drawable.ic_clock_black),
            SheetSelectionItem("19", "19:00", R.drawable.ic_clock_black),
            SheetSelectionItem("20", "20:00", R.drawable.ic_clock_black),
            SheetSelectionItem("21", "21:00", R.drawable.ic_clock_black),
            SheetSelectionItem("22", "22:00", R.drawable.ic_clock_black)
        )

        context?.let {
            SheetSelection.Builder(it)
                .title("Pilih Jam")
                .items(items)
                .showDraggedIndicator(true)
                .searchEnabled(false)
                .onItemClickListener { item, _ ->
                    edt_choose_time_car.setText(item.value)
                    TIME = item.value
                    edt_choose_time_car.error = null
                }
                .show()
        }
    }

    private fun chooseDriver() {
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
                .onItemClickListener { item, _ ->
                    edt_choose_driver_car.setText(item.value)
                    DRIVER = item.value
                    edt_choose_driver_car.error = null
                }
                .show()
        }
    }

    private fun cariMobil() {
        val date = edt_choose_date_car.text.toString().trim()
        val duration = edt_choose_duration_car.text.toString().trim()
        val time = edt_choose_time_car.text.toString().trim()
        val driver = edt_choose_driver_car.text.toString().trim()

        if (date.isEmpty()) {
            edt_choose_date_car.error = "Pemilihan tanggal penyewaan tidak boleh kosong!"
            context?.let {
                Toasty.error(
                    it,
                    "Pemilihan tanggal penyewaan tidak boleh kosong!",
                    Toast.LENGTH_SHORT,
                    true
                ).show()
            }
            return
        }

        if (duration.isEmpty()) {
            edt_choose_duration_car.error = "Pemilihan durasi penyewaan tidak boleh kosong!"
            context?.let {
                Toasty.error(
                    it,
                    "Pemilihan durasi penyewaan tidak boleh kosong!",
                    Toast.LENGTH_SHORT,
                    true
                ).show()
            }
            return
        }

        if (time.isEmpty()) {
            edt_choose_time_car.error = "Pemilihan jam penyewaan tidak boleh kosong!"
            context?.let {
                Toasty.error(
                    it,
                    "Pemilihan jam penyewaan tidak boleh kosong!",
                    Toast.LENGTH_SHORT,
                    true
                ).show()
            }
            return
        }

        if (driver.isEmpty()) {
            edt_choose_driver_car.error = "Pemilihan drive tidak boleh kosong!"
            context?.let {
                Toasty.error(
                    it,
                    "Pemilihan drive tidak boleh kosong!",
                    Toast.LENGTH_SHORT,
                    true
                ).show()
            }
            return
        }

        if (date.isNotEmpty() && duration.isNotEmpty() && time.isNotEmpty() && driver.isNotEmpty()
            && DATE != null && DURATION != null && TIME != null && DRIVER != null
        ) {
            covertDateToTimeMilis()
            val toReadyCard = DateBookingFragmentDirections
                .actionNavigationDateBookingCarFragmentToNavigationReadyCarFragment()
            toReadyCard.startDate = STARTDATE?.div(1000)!!
            toReadyCard.endDate = ENDDATE?.div(1000)!!
            toReadyCard.duration = DURATION!!
            toReadyCard.driver = DRIVER!!
            edt_choose_date_car.text.clear()
            edt_choose_driver_car.text.clear()
            edt_choose_time_car.text.clear()
            edt_choose_duration_car.text.clear()
            view?.findNavController()?.navigate(toReadyCard)
        }
    }

    private fun covertDateToTimeMilis() {
        val dateTime = "$DATE $TIME"
        val formater = SimpleDateFormat("dd-MM-yyyy hh:mm")
        val dates = formater.parse(dateTime)
        val duration = DURATION
        STARTDATE = dates?.time
        if (duration != null) {
            ENDDATE = STARTDATE?.plus(duration * 24 * 60 * 60 * 1000L)
        }
        Log.d("DatesTest dateandtime", "$dates")
        Log.d("DatesTest STARTDATE", "$STARTDATE")
        Log.d("DatesTest ENDDATE", "$ENDDATE")
    }
}
