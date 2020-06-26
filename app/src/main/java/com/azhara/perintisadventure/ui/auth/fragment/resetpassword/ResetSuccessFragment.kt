package com.azhara.perintisadventure.ui.auth.fragment.resetpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.azhara.perintisadventure.R
import kotlinx.android.synthetic.main.fragment_reset_success.*

/**
 * A simple [Fragment] subclass.
 */
class ResetSuccessFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_kembali_login.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_global_reset_to_loginFragment)
        )
    }

}
