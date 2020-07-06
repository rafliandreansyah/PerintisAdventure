package com.azhara.perintisadventure.ui.home.ui.bookingtour.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadventure.R
import kotlinx.android.synthetic.main.rv_item_facility.view.*

class FacilityAdapter(private val facilities: List<String>) : RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FacilityAdapter.FacilityViewHolder {
        return FacilityViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_facility, parent, false))
    }

    override fun getItemCount(): Int = facilities.size

    override fun onBindViewHolder(holder: FacilityAdapter.FacilityViewHolder, position: Int) {
        holder.itemView.tv_item_facility.text = facilities[position]
    }

    inner class FacilityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}