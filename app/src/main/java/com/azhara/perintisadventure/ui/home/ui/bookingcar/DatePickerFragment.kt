package com.azhara.perintisadventure.ui.home.ui.bookingcar

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener{

    private var dateListener: DialogDateListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)

        return context?.let { DatePickerDialog(it, this, year, month, date) }!!

    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dateListener?.onDialogSet(tag ,year, month, dayOfMonth)
    }

    interface DialogDateListener {
        fun onDialogSet(tag: String?, year: Int, month: Int, dayOfMonth: Int)
    }

}