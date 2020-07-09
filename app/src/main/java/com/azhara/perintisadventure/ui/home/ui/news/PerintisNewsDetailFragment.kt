package com.azhara.perintisadventure.ui.home.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.azhara.perintisadventure.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_perintis_news_detail.*

class PerintisNewsDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perintis_news_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imgUrl = PerintisNewsDetailFragmentArgs.fromBundle(arguments as Bundle).imgUrl
        val title = PerintisNewsDetailFragmentArgs.fromBundle(arguments as Bundle).title
        val content = PerintisNewsDetailFragmentArgs.fromBundle(arguments as Bundle).content
        val date = PerintisNewsDetailFragmentArgs.fromBundle(arguments as Bundle).date

        setData(imgUrl, title, content, date)
    }

    private fun setData(imgUrl: String?, title: String?, content: String?, date: String?) {
        context?.let { Glide.with(it).load(imgUrl).centerCrop().into(img_detail_news) }
        tv_title_detail_news.text = title
        tv_date_detail_news.text = date
        tv_content_detail_news.text = content
    }
}