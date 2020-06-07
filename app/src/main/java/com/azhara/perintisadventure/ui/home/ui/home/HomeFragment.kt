package com.azhara.perintisadventure.ui.home.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.Users
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var homeViewModel: HomeViewModel

    override fun onStart() {
        super.onStart()
        val user =homeViewModel.auth.currentUser
        if (user != null){
            loadingState(true)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        card_profile_home.setOnClickListener(this)
        card_booking_car.setOnClickListener(this)
        card_booking_tour.setOnClickListener(this)

        homeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]

        homeViewModel.loadDataUser()
        loadUserDoc()
    }

    @SuppressLint("SetTextI18n")
    private fun addData(user: Users){
        tv_text_name_home.text = "Hai ${user.name}!"
        if(user.imgUrl != null){
            context?.let {
                Glide.with(it)
                    .load(user.imgUrl)
                    .into(img_profile_home)
            }
        }
    }

    private fun loadUserDoc(){
        homeViewModel.loadUserDoc().observe(viewLifecycleOwner, Observer { data ->
            if (data!= null){
                addData(data)
                loadingState(false)
            }else{
                Toast.makeText(context, homeViewModel.errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadingState(state: Boolean){
        if (state){
            loading_home.visibility = View.VISIBLE
        }else{
            loading_home.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.card_profile_home -> {
                view?.findNavController()?.navigate(R.id.action_navigation_home_to_navigation_profile)
            }
            R.id.card_booking_car -> {
                view?.findNavController()?.navigate(R.id.action_navigation_home_to_navigation_date_booking_car_fragment)
            }
            R.id.card_booking_tour -> {
                view?.findNavController()?.navigate(R.id.action_navigation_home_to_navigation_booking_destination_fragment)
            }
        }
    }
}
