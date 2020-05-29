package com.azhara.perintisadventure.ui.home.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.Users
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

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

        homeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]

        card_profile_home.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_navigation_profile)
        )
        homeViewModel.loadDataUser()
        loadUserDoc()
    }

    private fun addData(user: Users){
        tv_text_name_home.text = "Hai ${user.name}!"
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
}
