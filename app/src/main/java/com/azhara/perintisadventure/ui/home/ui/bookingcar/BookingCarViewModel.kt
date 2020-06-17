package com.azhara.perintisadventure.ui.home.ui.bookingcar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.BookedDate
import com.azhara.perintisadventure.entity.Car
import com.google.firebase.firestore.FirebaseFirestore

class BookingCarViewModel : ViewModel(){

    private val db = FirebaseFirestore.getInstance()
    private val dataCar = MutableLiveData<List<Car>>()
    private val TAG = BookingCarViewModel::class.java.simpleName

    fun getDataCar() {
        val loadDb = db.collection("cars").whereEqualTo("statusReady", true)
        loadDb.get().addOnSuccessListener { documentSnapshot->
            val carsData = documentSnapshot.toObjects(Car::class.java)
            Log.d(TAG, "$carsData")
            dataCar.postValue(carsData)
        }
        loadDb.addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Log.w(TAG, exception.message)
            }
            if (snapshot != null){
                val carsData = snapshot.toObjects(Car::class.java)
                Log.d("$TAG snapshot", "$carsData")
                dataCar.postValue(carsData)
            }
        }
    }

    fun dataCar(): LiveData<List<Car>> = dataCar
}