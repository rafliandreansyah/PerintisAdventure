package com.azhara.perintisadventure.ui.home.ui.bookingtour.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.Tour
import com.google.firebase.firestore.FirebaseFirestore

class BookingTourViewModel : ViewModel(){

    private val db = FirebaseFirestore.getInstance()
    private val dataTour = MutableLiveData<List<Tour>>()
    private val TAG = BookingTourViewModel::class.java.simpleName

    fun getDataTour(){
        val tourDb = db.collection("tour")
        tourDb.addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Log.e(TAG, exception.message)
                return@addSnapshotListener
            }

            if (snapshot != null){
                val data = snapshot.toObjects(Tour::class.java)
                dataTour.postValue(data)
                Log.d("$TAG getData Tour", "$data")
            }
        }
    }

    fun dataTour(): LiveData<List<Tour>> = dataTour

}