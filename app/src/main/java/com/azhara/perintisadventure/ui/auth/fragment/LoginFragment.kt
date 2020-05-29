package com.azhara.perintisadventure.ui.auth.fragment

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController

import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var loginViewModel: LoginViewModel

    override fun onStart() {
        super.onStart()
        val users = loginViewModel.getSessionUsers()
        if (users != null){
            context?.startActivity(Intent(context, HomeActivity::class.java))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tv_toregister.setOnClickListener(this)
        tv_forgot_password.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        loginViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(LoginViewModel::class.java)
        loginState()
    }

    private fun login(){
        val email = edt_login_email.text.toString().trim()
        val password = edt_login_password.text.toString().trim()

        if (email.isEmpty()){
            showLoading(false)
            edt_login_email.error = "Email tidak boleh kosong!"
            return
        }
        if (password.isEmpty()){
            showLoading(false)
            edt_login_password.error = "Password tidak boleh kosong!"
            return
        }
        if (!email.isEmpty() && !password.isEmpty()){
            showLoading(true)
            loginViewModel.login(email, password)
            return
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_toregister -> {
                view?.findNavController()?.navigate(R.id.action_loginFragment_to_registerFragment)
            }
            R.id.tv_forgot_password -> {
                view?.findNavController()?.navigate(R.id.action_loginFragment_to_resetPasswordFragment)
            }
            R.id.btn_login -> {
                showLoading(true)
                login()
            }
        }
    }

    private fun showLoading(state: Boolean){
        if (state){
            loading.visibility = View.VISIBLE
        }else{
            loading.visibility = View.GONE
        }
    }

    private fun loginState(){
        activity?.let {
            loginViewModel.loginStatus().observe(it, Observer { statusLogin ->
                if (statusLogin == true){
                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }else{
                    showLoading(false)
                    tv_login_status.visibility = View.VISIBLE
                    tv_login_status.text = loginViewModel.errorMessage
                }
            })
        }
    }

}
