package com.azhara.perintisadventure.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.entity.BookingList
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_payment.*

/**
 * A simple [Fragment] subclass.
 */
class PaymentFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.tv_title_toolbar?.text = "Pembayaran"
        btn_home.setOnClickListener(this)

        val bookingId = PaymentFragmentArgs.fromBundle(arguments as Bundle).bookingListId
        val totalPrice = PaymentFragmentArgs.fromBundle(arguments as Bundle).totalPrice
        val uploadProofPayment = PaymentFragmentArgs.fromBundle(arguments as Bundle).uploadProofPayemnt
        val imgUrlProofPayment = PaymentFragmentArgs.fromBundle(arguments as Bundle).imgUrlProofPayment

        setData(totalPrice, uploadProofPayment, imgUrlProofPayment)
    }

    private fun setData(totalPrice: Long?, uploadProofPayment: Boolean?, imgUrlProofPayment: String?){
        tv_total_price_payment.text = "Rp. $totalPrice"
        if (uploadProofPayment == false){
            tv_status_upload_proof_payment.text = "Bukti pembayaran belum terupload."
        }
        if (imgUrlProofPayment != null && uploadProofPayment == true){
            Glide.with(this).load(imgUrlProofPayment).into(img_proof_payment)
            tv_status_upload_proof_payment.text = "Menunggu konfirmasi dari admin."
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_home -> {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_payment_to_navigation_home)
            }
        }
    }

}
