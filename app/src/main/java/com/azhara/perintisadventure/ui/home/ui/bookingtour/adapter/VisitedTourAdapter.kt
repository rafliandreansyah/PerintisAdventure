package com.azhara.perintisadventure.ui.home.ui.bookingtour.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.Facility
import com.azhara.perintisadventure.entity.VisitedTour
import kotlinx.android.synthetic.main.rv_item_visited_tour.view.*

class VisitedTourAdapter : ListAdapter<VisitedTour, VisitedTourAdapter.VisitedTourViewHolder>(DIFF_CALLBACK){

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VisitedTour>(){
            override fun areItemsTheSame(oldItem: VisitedTour, newItem: VisitedTour): Boolean {
                return oldItem.visitedTour == newItem.visitedTour
            }

            override fun areContentsTheSame(oldItem: VisitedTour, newItem: VisitedTour): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VisitedTourAdapter.VisitedTourViewHolder {
        return VisitedTourViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_visited_tour, parent, false))
    }

    override fun onBindViewHolder(holder: VisitedTourAdapter.VisitedTourViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VisitedTourViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(visitedTour: VisitedTour){
            with(itemView){
                tv_item_visited_tour.text = visitedTour.visitedTour
            }
        }
    }
}