package com.azhara.perintisadventure.ui.home.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.Slider
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.item_slider.view.*

class SliderAdapter(private val slider: List<Slider>): SliderViewAdapter<SliderAdapter.SliderViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapter.SliderViewHolder {
        return SliderViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_slider, parent, false))
    }

    override fun onBindViewHolder(viewHolder: SliderAdapter.SliderViewHolder?, position: Int) {
        viewHolder?.bind(slider[position])
    }

    override fun getCount(): Int = slider.size

    inner class SliderViewHolder(itemView: View): SliderViewAdapter.ViewHolder(itemView){
        fun bind(slider: Slider){
            with(itemView){
                Glide.with(itemView).load(slider.imgUrl).centerCrop().into(img_item_slider)
            }
        }
    }

}