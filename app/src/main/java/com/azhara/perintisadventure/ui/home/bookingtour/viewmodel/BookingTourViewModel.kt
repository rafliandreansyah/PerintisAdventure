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
        dateTour: Timestamp?, tourId: String?,
        totalPrice: Long?, pickupArea: String?, tourName: String?, duration: String?
    ) {

        val detailBookingTourUser = DetailBookingTourUser(
            dateTour, tourId, totalPrice, pickupArea, 0
        )

        val userDb = db.collection("users").document("$userId")
            .collection("bookingTour")

        userDb.add(detailBookingTourUser).addOnSuccessListener {
            val bookedDetailTourId = it.id
            userListBooking(
                bookedDetailTourId,
                tourName,
                totalPrice,
                dateTour,
                tourId,
                duration,
                pickupArea
            )
        }.addOnFailureListener {
            Log.e("$TAG error detailBooking", "userDb: ${it.message}")
            checkBooking.postValue(false)
        }
    }

    private fun userListBooking(
        bookedDetailTourId: String?, bookingName: String?, totalPrice: Long?, dateTour: Timestamp?, tourId: String?, duration: String?, pickupArea: String?
    ) {
        val dataUserList = BookingList(
            null,
            bookedDetailTourId,
            null,
            null,
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
            updateDataListIdUser(idList, bookedDetailTourId, dateTour, totalPrice, tourId, bookingName, duration, pickupArea)
        }.addOnFailureListener {
            Log.e("$TAG Error userDb", "UserListDb: ${it.message}")
            checkBooking.postValue(false)
        }
    }

    private fun updateDataListIdUser(
        idList: String?,
        idDetailBookingTourUser: String?,
        dateTour: Timestamp?,
        totalPrice: Long?,
        tourId: String?,
        tourName: String?,
        duration: String?,
        pickupArea: String?
    ) {
        val updateDataUserListDb = db.collection("users").document("$userId")
            .collection("listBooking").document("$idList")

        updateDataUserListDb.update("bookingListId", idList).addOnSuccessListener {

            getDataUsers(idList, idDetailBookingTourUser, userId, dateTour, totalPrice, tourId, tourName, duration,  pickupArea)
        }.addOnFailureListener {
            Log.e("$TAG Error updateUserDb", "UserListDb update bookingListId: ${it.message}")
            checkBooking.postValue(false)
        }
    }

    private fun getDataUsers(idListBookingTourUser: String?, idDetailBookingTourUser: String?, userId: String?,
                             dateTour: Timestamp?, totalPrice: Long?, tourId: String?, tourName: String?, duration: String?,
                             pickupArea: String?){
        val userDb = userId?.let { db.collection("users").document(it) }
        userDb?.get()?.addOnSuccessListener { document ->
            val userName = document["name"] as String
           addBookingTourData(idListBookingTourUser, idDetailBookingTourUser, userId, userName, dateTour, totalPrice, tourId, tourName, duration, pickupArea)
        }?.addOnFailureListener { e ->
            checkBooking.postValue(false)
            Log.e("$TAG Error getDataUsers", "UserListDb update bookingListId: ${e.message}")
        }
    }

    private fun addBookingTourData(idListBookingTourUser: String?, idDetailBookingTourUser: String?, userId: String?, userBookingName: String?,
                                   dateTour: Timestamp?, totalPrice: Long?, tourId: String?, tourName: String?, duration: String?,
                                    pickupArea: String?){

        val bookingTour = hashMapOf(
            "userId" to userId,
            "tourId" to tourId,
            "idListBookingTourUser" to idListBookingTourUser,
            "totalPrice" to totalPrice,
            "dateTour" to dateTour,
            "pickupArea" to pickupArea,
            "statusPayment" to false,
            "idDetailBookingTourUser" to idDetailBookingTourUser,
            "userBookingName" to userBookingName,
            "tourName" to tourName,
            "duration" to duration
        )

        val addBookingTour = db.collection("booking_tour")
        addBookingTour.add(bookingTour).addOnSuccessListener {
            checkBooking.postValue(true)
        }.addOnFailureListener {
            checkBooking.postValue(false)
        }
    }

    fun bookingListId(): LiveData<String> = bookingListId

    fun checkBooking(): LiveData<Boolean> = checkBooking

}