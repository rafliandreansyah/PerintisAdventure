package com.azhara.perintisadventure.ui.home.ui.payment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.BookingList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class PaymentViewModel : ViewModel(){

    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val statusUploadData = MutableLiveData<Boolean>()
    private val listBookingData = MutableLiveData<BookingList>()


    fun loadDataListBooking(listBookingId: String?){
        val dataUserListBookingDb = db.collection("users").document("$userId")
            .collection("listBooking").document("$listBookingId")
        dataUserListBookingDb.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("Payment getData", "Listen failed. $exception")
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("Payment getData", "Current data: ${snapshot.data}")
                val data = snapshot.toObject(BookingList::class.java)
                listBookingData.postValue(data)
            } else {
                Log.d("Payment getData", "Current data: null")
            }
        }
    }

    fun uploadProofPayment(imgByteArray: ByteArray, partnerId: String?, bookingPartnerId: String?,
                           listBookingId: String?, bookingType: Int?){
        var imgStorage = storage.reference.child("proofPayment")
            .child("$userId").child("car").child("$listBookingId")
        if (bookingType == 1){
            imgStorage = storage.reference.child("proofPayment")
                .child("$userId").child("tour").child("$listBookingId")
        }

        val uploadTask = imgStorage.putBytes(imgByteArray)
        uploadTask.addOnSuccessListener {
            Log.d("Payment", "Berhasil upload")
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful){
                    statusUploadData.postValue(false)
                    task.exception.let {
                        throw it!!
                    }
                }
                imgStorage.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful){
                    val downloadUri = it.result
                    updateDataUsersUploadPayment(downloadUri.toString(), listBookingId, partnerId, bookingPartnerId, bookingType)
                }else{
                    statusUploadData.postValue(false)
                }
            }
        }.addOnFailureListener {
            statusUploadData.postValue(false)
        }
    }

    fun dataListBooking(): LiveData<BookingList> = listBookingData

    private fun updateDataUsersUploadPayment(urlImgProofPayment: String?, listBookingId: String?, partnerId: String?, bookingPartnerId: String?, bookingType: Int?){
        val userDb = db.collection("users").document("$userId")
            .collection("listBooking").document("$listBookingId")

        val dataUpdate = hashMapOf(
            "imgUrlProofPayment" to "$urlImgProofPayment",
            "uploadProofPayment" to true
        )
        userDb.update(dataUpdate).addOnSuccessListener {
            Log.d("Payment", "Berhasil upload")
            updateDataPartnersUploadPayment(urlImgProofPayment, partnerId, bookingPartnerId, bookingType)
        }.addOnFailureListener {
            statusUploadData.postValue(false)
            Log.e("Payment update users", "Gagal ${it.message}")
        }
    }

    private fun updateDataPartnersUploadPayment(urlImgProofPayment: String?, partnerId: String?, bookingPartnerId: String?, bookingType: Int?){
        var partnerDb = db.collection("partners").document("$partnerId")
            .collection("bookingCar").document("$bookingPartnerId")
        if (bookingType == 1){
            partnerDb = db.collection("partners").document("$partnerId")
                .collection("bookingTour").document("$bookingPartnerId")
        }
        val dataUpdate = hashMapOf(
            "imgUrlProofPayment" to "$urlImgProofPayment",
            "uploadProofPayment" to true
        )
        partnerDb.update(dataUpdate).addOnSuccessListener {
            statusUploadData.postValue(true)
            Log.d("Payment Upload partner", "Berhasil")
        }.addOnFailureListener {
            statusUploadData.postValue(false)
            Log.e("Payment Upload partner", "Berhasil")
        }
    }

    fun statusUpload(): LiveData<Boolean> = statusUploadData

}