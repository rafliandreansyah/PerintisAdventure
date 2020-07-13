package com.azhara.perintisadventure.ui.home.bookingcar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.Car
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.rv_items_ready_car.view.*
import java.text.DecimalFormat

class CarAdapter : ListAdapter<Car, CarAdapter.CarViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Car>() {
            override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
                return oldItem.booked == newItem.booked
            }

            override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarAdapter.CarViewHolder {
        return CarViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_items_ready_car, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CarAdapter.CarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(car: Car?) {
            with(itemView) {
                tv_car_name.text = car?.carName
                tv_capacity.text = car?.capacity.toString()
                tv_car_year.text = car?.year.toString()
                tv_price_car.text = "Rp. ${decimalFormat(car?.price)} / Hari"
                tv_luggage.text = car?.luggage.toString()
                if (car?.gear!! == 0) {
                    tv_gear_ready_car.text = "Manual"
                } else {
                    tv_gear_ready_car.text = "Automatic"
                }
                Glide.with(context).load(car.imgUrl).into(img_car_item)
                cardview.setOnClickListener {
                    onItemClickCallBack?.onItemClick(car)
                }
            }

        }
    }

    private fun decimalFormat(price: Long?): String? {
        val formatDecimal = DecimalFormat("###,###,###")
        return formatDecimal.format(price)
    }

    interface OnItemClickCallBack {
        fun onItemClick(car: Car?)
    }

}