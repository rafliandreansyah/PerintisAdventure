package com.azhara.perintisadventure.ui.home.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.News
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.rv_item_news_home.view.*
import java.text.SimpleDateFormat
import java.util.*

class HomeNewsAdapter : ListAdapter<News, HomeNewsAdapter.HomeNewsViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnItemClicked(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeNewsAdapter.HomeNewsViewHolder {
        return HomeNewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_item_news_home, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeNewsAdapter.HomeNewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HomeNewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(news: News) {
            with(itemView) {
                val date = convertToLocalDate(news.date?.seconds)
                Glide.with(context).load(news.imgUrl).centerCrop().into(img_item_news_home)
                tv_item_title_news_home.text = "${news.title}"
                tv_item_date_news_home.text = date
                card_news_home.setOnClickListener {
                    onItemClickCallBack?.onItemClicked(news)
                }
            }
        }
    }

    private fun convertToLocalDate(date: Long?): String {
        // Convert timestamp to local time
        val calendar = Calendar.getInstance()
        val tz = calendar.timeZone
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        sdf.timeZone = tz
        val startSecondDate = date?.times(1000)?.let { Date(it) }
        return sdf.format(startSecondDate)
    }

    interface OnItemClickCallBack {
        fun onItemClicked(news: News)
    }
}