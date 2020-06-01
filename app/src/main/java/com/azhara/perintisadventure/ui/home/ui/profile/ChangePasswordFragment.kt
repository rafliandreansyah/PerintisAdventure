package com.azhara.perintisadventure.ui.home.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController

import com.azhara.perintisadventure.R
import com.mrntlu.toastie.Toastie
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.fragment_reset_password.*

/**
 * A simple [Fragment] subclass.
 */
class ChangePasswordFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ProfileViewModel::class.java]

        btn_save_change_password.setOnClickListener{
            changePassword()
            view.findNavController().navigate(R.id.action_global_navigation_edit_profile)
        }
        statusMessage()
    }

    private fun changePassword(){
        loading(true)
        val oldPass = edt_old_password.text.toString().trim()
        val newPass = edt_new_password.text.toString().trim()
        val newPassConfirm = edt_confirm_new_password.text.toString().trim()

        if (oldPass.isEmpty()){
            edt_old_password.error = "Password tidak boleh kosong!"
        }

        if (newPass.isEmpty()){
            edt_new_password.error = "Password baru tidak boleh kosong!"
        }

        if (newPassConfirm.isEmpty()){
            edt_confirm_new_password.error = "Konfirmasi password tidak boleh kosong!"
        }

        if (newPass != newPassConfirm){
            edt_confirm_new_password.error = "Password tidak sama!"
        }

        if (oldPass.isNotEmpty() && newPass.isNotEmpty() && newPassConfirm.isNotEmpty() && newPass == newPassConfirm){
            profileViewModel.changePassword(oldPass, newPass)
        }
    }

    private fun statusMessage(){
        profileViewModel.changePasswordState().observe(viewLifecycleOwner, Observer { state->
            if (state == false){
                loading(false)
                Toastie.error(context, profileViewModel.errorMessage, Toast.LENGTH_SHORT).show()

            }
        })

    }

    private fun loading(state: Boolean){
        if (state){
            loading_change_password.visibility = View.VISIBLE
        }else{
            loading_change_password.visibility = View.GONE
        }
    }

}
