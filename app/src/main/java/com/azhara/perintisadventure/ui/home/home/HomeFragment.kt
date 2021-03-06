package com.azhara.perintisadventure.ui.home.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.News
import com.azhara.perintisadventure.entity.User
import com.azhara.perintisadventure.ui.home.home.adapter.HomeNewsAdapter
import com.azhara.perintisadventure.ui.home.home.adapter.SliderAdapter
import com.azhara.perintisadventure.ui.home.home.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private var backPressedTime: Long? = 0
    private lateinit var toast: Toast
    override fun onResume() {
        super.onResume()
        val user = homeViewModel.auth.currentUser
        if (user != null) {
            loadingShimmer(true)
        }
        homeViewModel.loadDataUser()
        loadUserDoc()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {

            if (backPressedTime!! + 2000 > System.currentTimeMillis()) {
                toast.cancel()
                activity?.moveTaskToBack(true)
                activity?.finish()
            } else {
                toast = Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT)
                toast.show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        card_profile_home.setOnClickListener(this)
        card_booking_car.setOnClickListener(this)
        card_booking_tour.setOnClickListener(this)
        img_to_more_news.setOnClickListener(this)
        homeViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]

        val user = homeViewModel.auth.currentUser
        if (user != null) {
            loadingShimmer(true)
        }

        homeViewModel.loadDataUser()
        loadUserDoc()
        loadSlider()
    }

    private fun onItemNewsClicked(newsAdapter: HomeNewsAdapter) {
        newsAdapter.setOnItemClicked(object : HomeNewsAdapter.OnItemClickCallBack {
            override fun onItemClicked(news: News) {
                val toDetailNews = HomeFragmentDirections
                    .actionNavigationHomeToPerintisNewsDetailFragment()
                toDetailNews.imgUrl = news.imgUrl
                toDetailNews.content = news.content
                toDetailNews.date = convertToLocalDate(news.date?.seconds)
                toDetailNews.title = news.title
                view?.findNavController()?.navigate(toDetailNews)
            }

        })
    }

    @SuppressLint("SetTextI18n")
    private fun addData(user: User) {
        tv_text_name_home.text = "Hai ${user.name}!"
        if (user.imgUrl != null) {
            context?.let {
                Glide.with(it)
                    .load(user.imgUrl)
                    .into(img_profile_home)
            }
        }
    }

    private fun loadUserDoc() {
        homeViewModel.loadUserDoc().observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                addData(data)
                loadDataNews()
            } else {
                Toast.makeText(context, homeViewModel.errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadDataNews() {
        homeViewModel.loadNews()

        homeViewModel.dataNews().observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                layout_news.visibility = View.VISIBLE
                loadingShimmer(false)
                val newsHomeAdapter = HomeNewsAdapter()
                onItemNewsClicked(newsHomeAdapter)
                newsHomeAdapter.submitList(data)
                with(rv_news_home) {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    setHasFixedSize(true)
                    adapter = newsHomeAdapter
                }
            }
            if (data.isEmpty()) {
                layout_news.visibility = View.GONE
                loadingShimmer(false)
            }
        })
    }

    private fun loadingShimmer(state: Boolean) {
        if (state) {
            home_shimmer_profile.startShimmer()
            card_profile_home.visibility = View.GONE
            home_shimmer_profile.visibility = View.VISIBLE
        } else {
            card_profile_home.visibility = View.VISIBLE
            home_shimmer_profile.visibility = View.GONE
            home_shimmer_profile.stopShimmer()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.card_profile_home -> {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_home_to_navigation_profile)
            }
            R.id.card_booking_car -> {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_home_to_navigation_date_booking_car_fragment)
            }
            R.id.card_booking_tour -> {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_home_to_navigation_list_tour_fragment)
            }
            R.id.img_to_more_news -> {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_home_to_perintis_news_list_fragment)
            }
        }
    }

    private fun loadSlider() {
        homeViewModel.loadSlider()

        homeViewModel.dataSlider().observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                layout_slider.visibility = View.VISIBLE
                val sliderAdapater = SliderAdapter(data)
                sliderAdapater.notifyDataSetChanged()
                with(imageSlider) {
                    setSliderAdapter(sliderAdapater)
                    setIndicatorAnimation(IndicatorAnimationType.THIN_WORM)
                    setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                    autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
                    scrollTimeInSec = 4 //set scroll delay in seconds :
                    isAutoCycle = true
                    startAutoCycle()
                }
            }
            if (data.size == 1) {
                layout_slider.visibility = View.VISIBLE
                val sliderAdapater = SliderAdapter(data)
                sliderAdapater.notifyDataSetChanged()
                with(imageSlider) {
                    setSliderAdapter(sliderAdapater)
                    setIndicatorEnabled(false)
                    isAutoCycle = false
                }
            }
            if (data.isEmpty()) {
                val sliderAdapater = SliderAdapter(data)
                sliderAdapater.notifyDataSetChanged()
                layout_slider.visibility = View.GONE
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
