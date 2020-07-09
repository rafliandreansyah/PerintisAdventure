package com.azhara.perintisadventure.ui.home.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.News
import com.azhara.perintisadventure.ui.home.ui.news.adapter.NewsAdapter
import com.azhara.perintisadventure.ui.home.ui.news.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_perintis_news_list.*

class PerintisNewsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perintis_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[NewsViewModel::class.java]
        getDataNews(newsViewModel)
    }

    private fun getDataNews(newsViewModel: NewsViewModel) {
        newsViewModel.getNews()

        newsViewModel.datanNews().observe(viewLifecycleOwner, Observer { data ->
            if (data != null){
                setDataRecycleView(data)
            }
        })
    }

    private fun setDataRecycleView(data: List<News>){
        val newsAdapter = NewsAdapter()
        newsAdapter.submitList(data)
        with(rv_news){
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = newsAdapter
        }
    }
}