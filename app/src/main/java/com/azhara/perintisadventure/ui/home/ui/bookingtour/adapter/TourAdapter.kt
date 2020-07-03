package com.azhara.perintisadventure.ui.home.ui.bookingtour.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.Tour
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.rv_items_tour.view.*

class TourAdapter : ListAdapter<Tour, TourAdapter.TourViewHolder>(DIFF_CALLBACK){

    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Tour>(){
            override fun areItemsTheSame(oldItem: Tour, newItem: Tour): Boolean {
                return oldItem.price == newItem.price
            }

            override fun areContentsTheSame(oldItem: Tour, newItem: Tour): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TourAdapter.TourViewHolder {
        return TourViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_items_tour, parent, false))
    }

    override fun onBindViewHolder(holder: TourAdapter.TourViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TourViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(tour: Tour){
            with(itemView){
                tv_item_tour_name.text = tour.tourName
                tv_item_tour_capacity.text = "${tour.capacity}"
                tv_item_tour_price.text = "${tour.price}"
                tv_item_tour_location.text = tour.locationTour
                Glide.with(context).load(tour.imgUrl).into(img_item_tour)

                item_tour.setOnClickListener {
                    onItemClickCallBack?.onClicked(tour)
                }
            }
        }
    }

    interface OnItemClickCallBack{
        fun onClicked(tour: Tour)
    }

}