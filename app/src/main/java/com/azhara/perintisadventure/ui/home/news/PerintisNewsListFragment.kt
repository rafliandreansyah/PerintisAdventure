package com.azhara.perintisadventure.ui.home.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.News
import com.azhara.perintisadventure.ui.home.news.adapter.NewsAdapter
import com.azhara.perintisadventure.ui.home.news.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_perintis_news_list.*
import java.text.SimpleDateFormat
import java.util.*

class PerintisNewsListFragment : Fragment() {

    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perintis_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[NewsViewModel::class.java]
        newsAdapter = NewsAdapter()
        getDataNews(newsViewModel)
        onItemClick()
    }

    private fun getDataNews(newsViewModel: NewsViewModel) {
        newsViewModel.getNews()

        newsViewModel.datanNews().observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                setDataRecycleView(data)
            }
        })
    }

    private fun setDataRecycleView(data: List<News>) {
        newsAdapter.submitList(data)
        with(rv_news) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = newsAdapter
        }
    }

    private fun onItemClick() {
        newsAdapter.setOnClickCallBack(object : NewsAdapter.OnItemClickCallBack {
            override fun onItemCliked(news: News) {
                val toDetailNews = PerintisNewsListFragmentDirections
                    .actionNavigationPerintisNewsListFragmentToNavigationPerintisNewsDetailFragment()
                toDetailNews.title = news.title
                toDetailNews.content = news.content
                toDetailNews.imgUrl = news.imgUrl
                toDetailNews.date = convertToLocalDate(news.date?.seconds)
                view?.findNavController()?.navigate(toDetailNews)
            }
        })
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
}