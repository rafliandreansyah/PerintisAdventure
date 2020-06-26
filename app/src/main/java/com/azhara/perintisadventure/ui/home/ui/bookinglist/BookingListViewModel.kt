package com.azhara.perintisadventure.ui.home.ui.bookinglist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.BookingList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookingListViewModel : ViewModel() {

    private val TAG = BookingListViewModel::class.java.simpleName
    private val bookingList = MutableLiveData<List<BookingList>>()
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getDataBookingList(){
        val userId = auth.currentUser?.uid
        val bookingListDb = db.collection("users").document("$userId")
            .collection("listBooking")
        bookingListDb.addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Log.e("$TAG get data bookingList", "${exception.message}")
                return@addSnapshotListener
            }
            if (snapshot != null){
                Log.d("$TAG get data bookingList", "${snapshot.toObjects(BookingList::class.java)}")
                val bookingListData = snapshot.toObjects(BookingList::class.java)
                bookingList.postValue(bookingListData)
            }
        }
    }

    fun dataBookingList(): LiveData<List<BookingList>> = bookingList

}