package com.azhara.perintisadventure.ui.home

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.ui.home.ui.home.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val homeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_booked,
                R.id.navigation_profile,
                R.id.navigation_user_edit_profile,
                R.id.navigation_change_password,
                R.id.navigation_date_booking_car_fragment,
                R.id.navigation_ready_car_fragment,
                R.id.navigation_detail_ready_car_booking_fragment,
                R.id.navigation_payment,
                R.id.navigation_detail_booking_car_fragment,
                R.id.navigation_list_tour_fragment,
                R.id.navigation_detail_destination_fragment,
                R.id.navigation_detail_booking_tour_success_fragment,
                R.id.navigation_perintis_news_list_fragment,
                R.id.navigation_perintis_news_detail_fragment
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        toolbar_home.setupWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(false)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_home) {
                homeViewModel.loadSlider()

                homeViewModel.dataSlider().observe(this, Observer { data ->
                    if (data != null){
                        navView.visibility = View.VISIBLE
                        tv_title_toolbar.visibility = View.GONE
                        btn_back.visibility = View.GONE
                        img_toolbar_home.visibility = View.VISIBLE
                        img_toolbar_home.setImageResource(R.drawable.perintis_logo_white)
                        toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorAccentDark))
                        toolbar_home.elevation = 0F
                    }
                    if (data.isEmpty()){
                        navView.visibility = View.VISIBLE
                        tv_title_toolbar.visibility = View.GONE
                        btn_back.visibility = View.GONE
                        img_toolbar_home.visibility = View.VISIBLE
                        img_toolbar_home.setImageResource(R.drawable.perintis_logo)
                        toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                        toolbar_home.elevation = 4F
                    }
                })
            }
            if (destination.id == R.id.navigation_booked) {
                navView.visibility = View.VISIBLE
                tv_title_toolbar.visibility = View.VISIBLE
                tv_title_toolbar.text = "Pesanan"
                img_toolbar_home.visibility = View.GONE
                toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar_home.elevation = 4F
                btn_back.visibility = View.GONE
            }
            if (destination.id == R.id.navigation_profile) {
                navView.visibility = View.VISIBLE
                tv_title_toolbar.visibility = View.VISIBLE
                tv_title_toolbar.text = "Profil"
                img_toolbar_home.visibility = View.GONE
                toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar_home.elevation = 4F
                btn_back.visibility = View.GONE
            }
            if (destination.id == R.id.navigation_user_edit_profile) {
                navView.visibility = View.GONE
                tv_title_toolbar.text = "Edit profil"
                tv_title_toolbar.visibility = View.VISIBLE
                img_toolbar_home.visibility = View.GONE
                toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar_home.elevation = 4F
                btn_back.visibility = View.VISIBLE
                btn_back.setOnClickListener {
                    onBackPressed()
                }
            }
            if (destination.id == R.id.navigation_change_password) {
                navView.visibility = View.GONE
                tv_title_toolbar.text = "Password"
                tv_title_toolbar.visibility = View.VISIBLE
                img_toolbar_home.visibility = View.GONE
                toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar_home.elevation = 4F
                btn_back.visibility = View.VISIBLE
                btn_back.setOnClickListener {
                    onBackPressed()
                }
            }
            if (destination.id == R.id.navigation_date_booking_car_fragment) {
                navView.visibility = View.GONE
                tv_title_toolbar.visibility = View.VISIBLE
                img_toolbar_home.visibility = View.GONE
                toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar_home.elevation = 4F
                btn_back.visibility = View.VISIBLE
                btn_back.setOnClickListener {
                    onBackPressed()
                }
            }
            if (destination.id == R.id.navigation_ready_car_fragment) {
                navView.visibility = View.GONE
                tv_title_toolbar.visibility = View.VISIBLE
                img_toolbar_home.visibility = View.GONE
                toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar_home.elevation = 4F
                btn_back.visibility = View.VISIBLE
                btn_back.setOnClickListener {
                    onBackPressed()
                }
            }
            if (destination.id == R.id.navigation_detail_ready_car_booking_fragment) {
                navView.visibility = View.GONE
//                tv_title_toolbar.text = "Password"
                tv_title_toolbar.visibility = View.VISIBLE
                img_toolbar_home.visibility = View.GONE
                toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar_home.elevation = 4F
                btn_back.visibility = View.VISIBLE
                btn_back.setOnClickListener {
                    onBackPressed()
                }
            }
            if (destination.id == R.id.navigation_payment) {
                navView.visibility = View.GONE
                tv_title_toolbar.visibility = View.VISIBLE
                img_toolbar_home.visibility = View.GONE
                toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar_home.elevation = 4F
                btn_back.visibility = View.INVISIBLE
            }
            if (destination.id == R.id.navigation_detail_booking_car_fragment){
                navView.visibility = View.GONE
                tv_title_toolbar.visibility = View.VISIBLE
                tv_title_toolbar.text = "Detail Sewa Mobil"
                img_toolbar_home.visibility = View.GONE
                toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar_home.elevation = 4F
                btn_back.visibility = View.VISIBLE
                btn_back.setOnClickListener {
                    onBackPressed()
                }
            }
            if (destination.id == R.id.navigation_list_tour_fragment){
                navView.visibility = View.GONE
                tv_title_toolbar.visibility = View.VISIBLE
                tv_title_toolbar.text = "Pilih Paket Tour"
                img_toolbar_home.visibility = View.GONE
                toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar_home.elevation = 4F
                btn_back.visibility = View.VISIBLE
                btn_back.setOnClickListener {
                    onBackPressed()
                }
            }
            if (destination.id == R.id.navigation_detail_destination_fragment){
                navView.visibility = View.GONE
                tv_title_toolbar.visibility = View.VISIBLE
                tv_title_toolbar.text = "Detail Tour"
                img_toolbar_home.visibility = View.GONE
                toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar_home.elevation = 4F
                btn_back.visibility = View.VISIBLE
                btn_back.setOnClickListener {
                    onBackPressed()
                }
            }
            if (destination.id == R.id.navigation_detail_booking_tour_success_fragment){
                navView.visibility = View.GONE
                tv_title_toolbar.visibility = View.VISIBLE
                tv_title_toolbar.text = "Detail Tour"
                img_toolbar_home.visibility = View.GONE
                toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar_home.elevation = 4F
                btn_back.visibility = View.VISIBLE
                btn_back.setOnClickListener {
                    onBackPressed()
                }
            }
            if (destination.id == R.id.navigation_perintis_news_list_fragment){
                navView.visibility = View.GONE
                tv_title_toolbar.visibility = View.VISIBLE
                tv_title_toolbar.text = "Perintis News"
                img_toolbar_home.visibility = View.GONE
                toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar_home.elevation = 4F
                btn_back.visibility = View.VISIBLE
                btn_back.setOnClickListener {
                    onBackPressed()
                }
            }
            if (destination.id == R.id.navigation_perintis_news_detail_fragment){
                navView.visibility = View.GONE
                tv_title_toolbar.visibility = View.VISIBLE
                tv_title_toolbar.text = "Detail News"
                img_toolbar_home.visibility = View.GONE
                toolbar_home.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar_home.elevation = 4F
                btn_back.visibility = View.VISIBLE
                btn_back.setOnClickListener {
                    onBackPressed()
                }
            }
        }

    }
}
