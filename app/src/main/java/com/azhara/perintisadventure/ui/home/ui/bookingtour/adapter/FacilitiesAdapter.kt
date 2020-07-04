package com.azhara.perintisadventure.ui.home.ui.bookingtour.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.Facility
import kotlinx.android.synthetic.main.rv_item_facility.view.*

class FacilitiesAdapter : ListAdapter<Facility, FacilitiesAdapter.FacilitiesViewHolder>(DIFF_CALLBACK){

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Facility>(){
            override fun areItemsTheSame(oldItem: Facility, newItem: Facility): Boolean {
                return oldItem.facility == newItem.facility
            }

            override fun areContentsTheSame(oldItem: Facility, newItem: Facility): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FacilitiesAdapter.FacilitiesViewHolder {
        return FacilitiesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_facility, parent, false))
    }

    override fun onBindViewHolder(holder: FacilitiesAdapter.FacilitiesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FacilitiesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(facility: Facility){
            with(itemView){
                tv_item_facility.text = facility.facility
            }
        }
    }

}