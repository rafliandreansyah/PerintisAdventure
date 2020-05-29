package com.azhara.perintisadventure.ui.auth.fragment.resetpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

import com.azhara.perintisadventure.R
import kotlinx.android.synthetic.main.fragment_reset_password.*

/**
 * A simple [Fragment] subclass.
 */
class ResetPasswordFragment : Fragment() {

    private lateinit var resetPasswordViewModel: ResetPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetPasswordViewModel = ViewModelProvider(this,
            ViewModelProvider.NewInstanceFactory())[ResetPasswordViewModel::class.java]

        btn_reset.setOnClickListener {
            reset()
        }
        resetState()
    }

    private fun reset(){
        loading(true)
        val email = edt_reset_email.text.toString().trim()
        if (email.isEmpty()){
            loading(false)
            edt_reset_email.error = "Kolom Email Tidak Boleh Kosong!"
        }

        if (email.isNotEmpty()){
            resetPasswordViewModel.resetPassword(email)
        }
    }

    private fun resetState(){
        resetPasswordViewModel.resetPasswordState().observe(viewLifecycleOwner, Observer { state ->
            if (state == true){
                loading(false)
                view?.findNavController()?.navigate(R.id.action_resetPasswordFragment_to_resetSuccessFragment)
            }else{
                loading(false)
                tv_reset_error.visibility = View.VISIBLE
                tv_reset_error.text = resetPasswordViewModel.errorMessage
            }
        })
    }

    private fun loading(state: Boolean){
        if (state){
            loading_reset_password.visibility = View.VISIBLE
        }else{
            loading_reset_password.visibility = View.GONE
        }
    }

}
