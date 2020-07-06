package com.azhara.perintisadventure.ui.home.ui.bookingtour.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadventure.R
import kotlinx.android.synthetic.main.rv_item_visited_tour.view.*

class VisitedTourAdapter (private val visitedTour: List<String>): RecyclerView.Adapter<VisitedTourAdapter.VisitedTourViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VisitedTourAdapter.VisitedTourViewHolder {
        return VisitedTourViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_visited_tour, parent, false))
    }

    override fun getItemCount(): Int = visitedTour.size

    override fun onBindViewHolder(holder: VisitedTourAdapter.VisitedTourViewHolder, position: Int) {
        holder.itemView.tv_item_visited_tour.text = visitedTour[position]
    }

    inner class VisitedTourViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}