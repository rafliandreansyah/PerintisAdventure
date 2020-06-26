package com.azhara.perintisadventure.ui.auth.fragment.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_success_register.*

/**
 * A simple [Fragment] subclass.
 */
class SuccessRegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_success_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_register_to_login.setOnClickListener {
            startActivity(Intent(context, HomeActivity::class.java))
        }

    }

}
