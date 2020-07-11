package com.azhara.perintisadventure.ui.home.ui.payment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.azhara.perintisadventure.R
import com.azhara.perintisadventure.ui.home.ui.payment.viewmodel.PaymentViewModel
import com.bumptech.glide.Glide
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_payment.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 */
class PaymentFragment : Fragment(), View.OnClickListener {

    private lateinit var paymentViewModel: PaymentViewModel
    private var imgUri: Uri? = null
    private var bitmapImg: Bitmap? = null
    private var requestImg = 1
    private var listBookingId: String? = null
    private var partnerId: String? = null
    private var bookingPartnerId: String? = null
    private var bookingType: Int? = 0

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
        tv_choose_image.setOnClickListener(this)
        btn_upload_proof_payment.setOnClickListener(this)
        paymentViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[PaymentViewModel::class.java]
        listBookingId = PaymentFragmentArgs.fromBundle(arguments as Bundle).bookingListId
        val totalPrice = PaymentFragmentArgs.fromBundle(arguments as Bundle).totalPrice
        val uploadProofPayment = PaymentFragmentArgs.fromBundle(arguments as Bundle).uploadProofPayemnt
        val imgUrlProofPayment = PaymentFragmentArgs.fromBundle(arguments as Bundle).imgUrlProofPayment
        bookingType = PaymentFragmentArgs.fromBundle(arguments as Bundle).bookingType
        loadDataBookingList(listBookingId)
        setData(totalPrice, uploadProofPayment, imgUrlProofPayment)
        getDataBankAccount()
    }

    private fun setData(totalPrice: Long?, uploadProofPayment: Boolean?, imgUrlProofPayment: String?){

        tv_total_price_payment.text = "Rp. ${decimalFormat(totalPrice)}"

        if (uploadProofPayment == false){
            tv_status_upload_proof_payment.text = "Bukti pembayaran belum terupload."
        }
        if (imgUrlProofPayment != null && uploadProofPayment == true){
            Glide.with(this).load(imgUrlProofPayment).into(img_proof_payment)
            tv_status_upload_proof_payment.text = "Menunggu konfirmasi dari admin."
            context?.let { ContextCompat.getColor(it, R.color.colorYellow) }?.let {
                tv_status_upload_proof_payment.setTextColor(
                    it
                )
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_home -> {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_payment_to_navigation_home)
            }
            R.id.tv_choose_image -> {
                openGallery()
            }
            R.id.btn_upload_proof_payment -> {
                btn_upload_proof_payment.isEnabled = false
                uploadToServer()
            }
        }
    }

    private fun loadDataBookingList(listBookingId: String?){
        loading(true)
        paymentViewModel.loadDataListBooking(listBookingId)

        paymentViewModel.dataListBooking().observe(viewLifecycleOwner, Observer { data->
            if (data != null){
                loading(false)
                partnerId = data.partnerId
                bookingPartnerId = data.bookingDbPartnerId
                if (data.imgUrlProofPayment != null || data.imgUrlProofPayment != ""){
                    activity?.let { Glide.with(it).load(data.imgUrlProofPayment).into(img_proof_payment) }

                }
            }else{
                Toast.makeText(context, "Data Kosong!", Toast.LENGTH_SHORT).show()
                loading(false)
            }
        })
    }

//    fun uploadProofPayment(imgByteArray: ByteArray, partnerId: String?, bookingCarPartnerId: String?,
//                           listBookingId: String?)
    private fun uploadToServer(){
        if (partnerId != null && bookingPartnerId != null && listBookingId != null && bitmapImg != null){
            loading(true)
            paymentViewModel.uploadProofPayment(imageByteArray(bitmapImg), partnerId, bookingPartnerId, listBookingId, bookingType)
        }
        paymentViewModel.statusUpload().observe(viewLifecycleOwner, Observer { data ->
            if (data == true){
                loading(false)
                view?.findNavController()?.navigate(R.id.action_navigation_payment_to_navigation_home)
            }else{
                loading(false)
                context?.let { Toasty.error(it, "Gagal upload data", Toast.LENGTH_SHORT, true).show() }
            }
        })
    }

    private fun imageByteArray(bitmap: Bitmap?): ByteArray {
        val bitmapImage = bitmap //get file bitmap
        val bitmapCompress = bitmapImage?.let { resizeBitmap(it, 200) } //resize bitmap file
        val baos = ByteArrayOutputStream()
        bitmapCompress?.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            baos
        ) //compress bitmap extension to JPEG
        val data = baos.toByteArray() //compress to byteArray

        return data
    }

    // resize filebitmap with specific size
    private fun resizeBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width //get width image
        var height = image.height //get heigh image

        val bitMapRatio = width.toFloat() / height.toFloat()
        if (bitMapRatio > 1) {
            width = maxSize
            height = (width / bitMapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitMapRatio).toInt()
        }

        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    // Intent open gallery
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestImg)
    }

    // State Loading
    private fun loading(state: Boolean) {
        if (state) {
            loading_background_payment.visibility = View.VISIBLE
            loading_payment.visibility = View.VISIBLE
            loading_payment.playAnimation()
        } else {
            loading_background_payment.visibility = View.INVISIBLE
            loading_payment.visibility = View.INVISIBLE
            loading_payment.cancelAnimation()
            btn_upload_proof_payment.isEnabled = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == requestImg && data != null){
            imgUri = data.data
            activity?.let { Glide.with(it).load(imgUri).into(img_proof_payment) }

            if (imgUri != null){
                try {
                    if (Build.VERSION.SDK_INT < 28){
                        val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imgUri)
                        bitmapImg = bitmap
                    }else{
                        val source =
                            activity?.contentResolver?.let { ImageDecoder.createSource(it, imgUri!!) }
                        val bitmap = source?.let { ImageDecoder.decodeBitmap(it) }
                        bitmapImg = bitmap
                    }
                }catch (e: Exception){
                    Log.e("PaymentFragment", "${e.message}")
                }
            }
        }
    }

    private fun getDataBankAccount(){
        loading(true)
        paymentViewModel.loadBankAccount()

        paymentViewModel.bankAccountData().observe(viewLifecycleOwner, Observer { data ->
            if (data != null){
                loading(false)
                tv_bank_name.text = data.bankName
                tv_no_rekening.text = data.noRekening
                tv_bank_code.text = data.kodeBank
                tv_atas_nama.text = data.atasNama
            }
        })
    }

    private fun decimalFormat(price: Long?): String?{
        val formatDecimal = DecimalFormat("###,###,###")
        return formatDecimal.format(price)
    }

}
