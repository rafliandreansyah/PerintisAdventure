package com.azhara.perintisadventure.ui.home.bookingtour.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.BookingList
import com.azhara.perintisadventure.entity.BookingTour
import com.azhara.perintisadventure.entity.DetailBookingTourUser
import com.azhara.perintisadventure.entity.Tour
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookingTourViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val dataTour = MutableLiveData<List<Tour>>()
    private val TAG = BookingTourViewModel::class.java.simpleName
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val checkBooking = MutableLiveData<Boolean>()
    private val bookingListId = MutableLiveData<String>()

    fun getDataTour() {
        val tourDb = db.collection("tour").whereEqualTo("statusReady", true)
        tourDb.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e(TAG, exception.message)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val updateDataTour = ArrayList<Tour>()
                val data = snapshot.documents
                data.forEach {
                    val tourUpdate = it.toObject(Tour::class.java)
                    if (tourUpdate != null) {
                        tourUpdate.tourId = it.id
                        updateDataTour.add(tourUpdate)
                    }
                }
                dataTour.postValue(updateDataTour)
                Log.d("$TAG getData Tour", "$updateDataTour")
            }
        }
    }

    fun dataTour(): LiveData<List<Tour>> = dataTour

    fun booking(
        dateTour: Timestamp?, tourId: String?, partnerId: String?,
        totalPrice: Long?, pickupArea: String?, tourName: String?
    ) {

        val detailBookingTourUser = DetailBookingTourUser(
            dateTour, tourId, partnerId, totalPrice, pickupArea, 0
        )

        val userDb = db.collection("users").document("$userId")
            .collection("bookingTour")

        userDb.add(detailBookingTourUser).addOnSuccessListener {
            val bookedTourId = it.id
            bookingDataPartner(
                tourId,
                totalPrice,
                dateTour,
                pickupArea,
                partnerId,
                bookedTourId,
                tourName
            )
        }.addOnFailureListener {
            Log.e("$TAG error detailBooking", "userDb: ${it.message}")
            checkBooking.postValue(false)
        }
    }

    private fun bookingDataPartner(
        tourId: String?, totalPrice: Long?, dateTour: Timestamp?, pickupArea: String?,
        partnerId: String?, bookedTourId: String?, tourName: String?
    ) {

        val dataBookingPartner = BookingTour(
            userId,
            tourId,
            null,
            totalPrice,
            dateTour,
            pickupArea,
            false,
            0,
            false,
            null
        )

        val partnerDb = db.collection("partners").document("$partnerId")
            .collection("bookingTour")

        partnerDb.add(dataBookingPartner).addOnSuccessListener {
            val bookingDbTourPartnerId = it.id
            userListBooking(
                bookedTourId,
                partnerId,
                bookingDbTourPartnerId,
                tourName,
                totalPrice,
                dateTour
            )
        }.addOnFailureListener {
            Log.e("$TAG Error dbPartner", "Data booking partner: ${it.message}")
            checkBooking.postValue(false)
        }
    }

    private fun userListBooking(
        bookingTourId: String?, partnerId: String?, bookingDbTourPartnerId: String?,
        bookingName: String?, totalPrice: Long?, dateTour: Timestamp?
    ) {
        val dataUserList = BookingList(
            null,
            bookingTourId,
            partnerId,
            bookingDbTourPartnerId,
            bookingName,
            totalPrice,
            dateTour,
            null,
            1,
            statusPayment = false,
            uploadProofPayment = false
        )

        val userListDb = db.collection("users").document("$userId")
            .collection("listBooking")

        userListDb.add(dataUserList).addOnSuccessListener {
            val idList = it.id
            bookingListId.postValue(idList)
            updateDataListIdUser(partnerId, bookingDbTourPartnerId, idList)
        }.addOnFailureListener {
            Log.e("$TAG Error userDb", "UserListDb: ${it.message}")
            checkBooking.postValue(false)
        }
    }

    private fun updateDataListIdUser(
        partnerId: String?,
        bookingDbPartnerId: String?,
        idList: String?
    ) {
        val updateDataUserListDb = db.collection("users").document("$userId")
            .collection("listBooking").document("$idList")

        updateDataUserListDb.update("bookingListId", idList).addOnSuccessListener {

            getIdListUserToPartner(partnerId, bookingDbPartnerId, idList)
        }.addOnFailureListener {
            Log.e("$TAG Error updateUserDb", "UserListDb update bookingListId: ${it.message}")
            checkBooking.postValue(false)
        }
    }

    private fun getIdListUserToPartner(
        partnerId: String?,
        bookingDbPartnerId: String?,
        idList: String?
    ) {
        val getIdListUserToPartner = db.collection("partners").document("$partnerId")
            .collection("bookingTour").document("$bookingDbPartnerId")

        getIdListUserToPartner.update("bookingListUserId", idList).addOnSuccessListener {
            checkBooking.postValue(true)
        }.addOnFailureListener {
            Log.e("$TAG Error partnerDb", "PartnerDb get id list user: ${it.message}")
            checkBooking.postValue(false)
        }
    }

    fun bookingListId(): LiveData<String> = bookingListId

    fun checkBooking(): LiveData<Boolean> = checkBooking

}