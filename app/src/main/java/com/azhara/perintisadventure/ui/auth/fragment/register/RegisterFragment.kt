package com.azhara.perintisadventure.ui.auth.fragment.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

import com.azhara.perintisadventure.R
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment(), View.OnClickListener {

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_tologin.setOnClickListener(this)
        btn_register.setOnClickListener(this)

        registerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[RegisterViewModel::class.java]

        stateRegister()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_tologin -> {
                view?.findNavController()?.navigate(R.id.action_global_register_to_loginFragment)
            }
            R.id.btn_register -> {
                register()
            }
        }
    }

    private fun register() {
        loading(true)
        val email = edt_register_email.text.toString().trim()
        val name = edt_register_name.text.toString().trim()
        val phone = edt_register_phone_number.text.toString().trim()
        val password = edt_register_password.text.toString().trim()
        val passwordConfirm = edt_register_password_confirm.text.toString().trim()

        if (email.isEmpty()){
            loading(false)
            edt_register_email.error = "Kolom Email tidak boleh kosong!"
        }

        if (name.isEmpty()){
            loading(false)
            edt_register_name.error = "Kolom Nama tidak boleh kosong!"
        }

        if (phone.isEmpty()){
            loading(false)
            edt_register_phone_number.error = "No. Telephone tidak boleh kosong!"
        }

        if (password.isEmpty()){
            loading(false)
            edt_register_password.error = "Password tidak boleh kosong!"
        }

        if (passwordConfirm.isEmpty()){
            loading(false)
            edt_register_password_confirm.error = "Konfirmasi Password tidak boleh kosong!"
        }

        if (password != passwordConfirm){
            loading(false)
            edt_register_password_confirm.error = "Password tidak sama!"
        }

        if (email.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()
            && password.isNotEmpty() && passwordConfirm.isNotEmpty()
            && password == passwordConfirm){
            registerViewModel.register(email, name, phone, password)
        }
    }

    private fun loading(state: Boolean){
        if (state){
            loading_register.visibility = View.VISIBLE
        }else{
            loading_register.visibility = View.GONE
        }
    }

    private fun stateRegister(){
        registerViewModel.stateRegister().observe(viewLifecycleOwner, Observer { state ->
            if (state){
                loading(false)
                view?.findNavController()?.navigate(R.id.action_registerFragment_to_successRegisterFragment)
            }else{
                loading(false)
                tv_error_register.visibility = View.VISIBLE
                tv_error_register.text = registerViewModel.errorMessage
            }
        })
    }

}
