package com.azhara.perintisadventure.ui.home.ui.bookinglist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.BookingList
import kotlinx.android.synthetic.main.rv_item_listbooking.view.*
import java.text.SimpleDateFormat
import java.util.*

class BookingListAdapter : ListAdapter<BookingList, BookingListAdapter.BookingListViewHolder>(DIFF_CALLBACK){

    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookingList>(){
            override fun areItemsTheSame(oldItem: BookingList, newItem: BookingList): Boolean {
                return oldItem.uploadProofPayment == newItem.uploadProofPayment
            }

            override fun areContentsTheSame(oldItem: BookingList, newItem: BookingList): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookingListAdapter.BookingListViewHolder {
        return BookingListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_listbooking, parent, false))
    }

    override fun onBindViewHolder(holder: BookingListAdapter.BookingListViewHolder, position: Int) = holder.bind(getItem(position))

    inner class BookingListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(bookingList: BookingList?){
            with(itemView){
                val startDate = bookingList?.startDate?.seconds?.let { convertToLocalDate(it) }
                tv_item_list_booking_name.text = bookingList?.bookingName
                tv_item_list_booking_date.text = startDate
                tv_item_list_booking_total_price.text = "Rp. ${bookingList?.totalPrice}"
                if(bookingList?.uploadProofPayment == false){
                    tv_item_list_booking_status_payment.background = ContextCompat.getDrawable(context, R.drawable.background_color_orange)
                    tv_item_list_booking_status_payment.text = "Menunggu pembayaran"
                }
                if(bookingList?.uploadProofPayment == true && bookingList.imgUrlProofPayment != null){
                    tv_item_list_booking_status_payment.background = ContextCompat.getDrawable(context, R.drawable.background_color_yellow)
                    tv_item_list_booking_status_payment.text = "Menunggu konfirmasi"
                }
                if (bookingList?.uploadProofPayment == true && bookingList.imgUrlProofPayment != null && bookingList.statusPayment == true){
                    tv_item_list_booking_status_payment.background = ContextCompat.getDrawable(context, R.drawable.background_color_green)
                    tv_item_list_booking_status_payment.text = "Pemesanan berhasil"
                }

                if (bookingList?.bookingType == 0){
                    tv_item_list_booking_type.text = "Pemesanan Mobil"
                }
                if(bookingList?.bookingType == 1){
                    tv_item_list_booking_type.text = "Pemesanan Wisata"
                }

                card_list_booking.setOnClickListener {
                    onItemClickCallBack?.onItemClicked(bookingList)
                }

            }
        }
    }

    private fun convertToLocalDate(date: Long): String {
        // Convert timestamp to local time
        val calendar = Calendar.getInstance()
        val tz = calendar.timeZone
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        sdf.timeZone = tz
        val startSecondDate = Date(date * 1000)
        return sdf.format(startSecondDate)
    }

    interface OnItemClickCallBack{
        fun onItemClicked(item: BookingList?)
    }

}