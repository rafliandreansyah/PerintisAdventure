package com.azhara.perintisadventure.ui.home.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.News
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.rv_item_list_news.view.*
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter : ListAdapter<News, NewsAdapter.NewsViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_item_list_news, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewsAdapter.NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(news: News) {
            with(itemView) {
                val date = convertToLocalDate(news.date?.seconds)
                Glide.with(context).load(news.imgUrl).centerCrop().into(img_item_list_news)
                tv_item_title_list_news.text = "${news.title}"
                tv_item_date_list_news.text = date
                tv_item_content_list_news.text = "${news.content}"

                card_news_list_item.setOnClickListener {
                    onItemClickCallBack?.onItemCliked(news)
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
        fun onItemCliked(news: News)
    }

}