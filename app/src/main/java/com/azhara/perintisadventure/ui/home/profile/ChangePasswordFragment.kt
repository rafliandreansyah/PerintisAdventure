package com.azhara.perintisadventure.ui.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.ui.home.profile.viewmodel.ProfileViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_change_password.*

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

        profileViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ProfileViewModel::class.java]

        btn_save_change_password.setOnClickListener {
            btn_save_change_password.isEnabled = false
            changePassword()
        }
        changePassMessage()
    }

    private fun changePassword() {
        loading(true)
        val oldPass = edt_old_password.text.toString().trim()
        val newPass = edt_new_password.text.toString().trim()
        val newPassConfirm = edt_confirm_new_password.text.toString().trim()

        if (oldPass.isEmpty()) {
            edt_old_password.error = "Password tidak boleh kosong!"
        }

        if (newPass.isEmpty()) {
            edt_new_password.error = "Password baru tidak boleh kosong!"
        }

        if (newPassConfirm.isEmpty()) {
            edt_confirm_new_password.error = "Konfirmasi password tidak boleh kosong!"
        }

        if (newPass != newPassConfirm) {
            edt_confirm_new_password.error = "Password tidak sama!"
        }

        if (oldPass.isNotEmpty() && newPass.isNotEmpty() && newPassConfirm.isNotEmpty() && newPass == newPassConfirm) {
            profileViewModel.changePassword(oldPass, newPass)
        }
    }

    private fun changePassMessage() {
        profileViewModel.changePassMessage().observe(viewLifecycleOwner, Observer { msg ->
            edt_old_password.text.clear()
            edt_new_password.text.clear()
            edt_confirm_new_password.text.clear()
            if (msg == "Password berhasil di update") {
                loading(false)
                context?.let { Toasty.success(it, msg, Toast.LENGTH_LONG, true).show() }
            } else {
                loading(false)
                context?.let { Toasty.error(it, msg, Toast.LENGTH_LONG, true).show() }
            }
        })
    }

    private fun loading(state: Boolean) {
        if (state) {
            loading_background_change_password.visibility = View.VISIBLE
            loading_change_password.visibility = View.VISIBLE
            loading_change_password.playAnimation()
        } else {
            loading_background_change_password.visibility = View.INVISIBLE
            loading_change_password.visibility = View.INVISIBLE
            loading_change_password.cancelAnimation()
            btn_save_change_password.isEnabled = true
        }
    }

}
