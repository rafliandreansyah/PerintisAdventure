package com.azhara.perintisadventure.ui.home.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.Users
import com.azhara.perintisadventure.ui.auth.AuthActivity
import com.bumptech.glide.Glide
import com.mrntlu.toastie.Toastie
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onStart() {
        super.onStart()
        loading(true)
        profileViewModel.getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(this, ViewModelProvider
            .NewInstanceFactory()).get(ProfileViewModel::class.java)

        btn_edit_profile.setOnClickListener(this)
        btn_logout.setOnClickListener(this)

        getDataUser()
    }

    private fun getDataUser(){
        profileViewModel.dataUser().observe(viewLifecycleOwner, Observer { data->
            if (data != null){
                tv_name_profile.text = data.name
                tv_email_profile.text = data.email
                tv_phone_profile.text = data.phone
                activity?.let { Glide.with(it).load(data.imgUrl).into(img_profile) }
                loading(false)
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_edit_profile -> {
                view?.findNavController()?.navigate(R.id.action_navigation_profile_to_navigation_edit_profile)
            }
            R.id.btn_logout -> {
                profileViewModel.signOut()
                startActivity(Intent(context, AuthActivity::class.java))
                activity?.finish()
            }
        }
    }

    private fun loading(state: Boolean){
        if (state){
            loading_profile.visibility = View.VISIBLE
        }else{
            loading_profile.visibility = View.GONE
        }
    }
}
