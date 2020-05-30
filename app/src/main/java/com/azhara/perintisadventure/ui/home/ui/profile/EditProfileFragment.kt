package com.azhara.perintisadventure.ui.home.ui.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController

import com.azhara.perintisadventure.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*

/**
 * A simple [Fragment] subclass.
 */
class EditProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onStart() {
        super.onStart()
        profileViewModel.getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_change_password.setOnClickListener(this)
        btn_save_edt_profile.setOnClickListener(this)
        profileViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ProfileViewModel::class.java]

        setOldDataUser()
    }

    private fun setOldDataUser(){
        profileViewModel.dataUser().observe(viewLifecycleOwner, Observer { data ->
            if (data != null){
                edt_name.setText(data.name)
                edt_email.setText(data.email)
                edt_phone.setText(data.phone)
            }
        })
    }

    private fun updateData(){
        val name = edt_name.text.toString().trim()
        val email = edt_email.text.toString().trim()
        val phone = edt_phone.text.toString().trim()

        if (name.isEmpty()){
            edt_name.error = "Name tidak boleh kosong!"
        }
        if (email.isEmpty()){
            edt_email.error = "Email tidak boleh kosong!"
        }
        if (phone.isEmpty()){
            edt_phone.error = "Phone tidak boleh kosong!"
        }

        if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()){
            profileViewModel.editDataUser(name, email, phone)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_change_password -> {
                view?.findNavController()?.navigate(R.id.action_navigation_user_edit_profile_to_navigation_change_password)
            }
            R.id.btn_save_edt_profile -> {
                updateData()
            }
        }
    }

    private fun loading(state: Boolean){

    }

}
