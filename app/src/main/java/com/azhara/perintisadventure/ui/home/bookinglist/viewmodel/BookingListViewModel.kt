package com.azhara.perintisadventure.ui.home.bookinglist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookingListViewModel : ViewModel() {

    private val TAG = BookingListViewModel::class.java.simpleName
    private val bookingList = MutableLiveData<List<BookingList>>()
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val bookingCarDetail = MutableLiveData<DetailBookingCarUser>()
    private val bookingTourDetail = MutableLiveData<DetailBookingTourUser>()
    private val car = MutableLiveData<Car>()
    private val tour = MutableLiveData<Tour>()
    private val userId = auth.currentUser?.uid
    fun getDataBookingList() {
        val bookingListDb = db.collection("users").document("$userId")
            .collection("listBooking")
        bookingListDb.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("$TAG get data bookingList", "${exception.message}")
                return@addSnapshotListener
            }
            if (snapshot != null) {
                Log.d("$TAG get data bookingList", "${snapshot.toObjects(BookingList::class.java)}")
                val bookingListData = snapshot.toObjects(BookingList::class.java)
                bookingList.postValue(bookingListData)
            }
        }
    }

    fun dataBookingList(): LiveData<List<BookingList>> = bookingList

    fun getDataDetailBookingCar(bookingId: String?) {
        val bookingCarDb = db.collection("users").document("$userId")
            .collection("bookingCar").document("$bookingId")
        bookingCarDb.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("$TAG get data bookingCar", "${exception.message}")
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d(
                    "$TAG get data bookingCar",
                    "${snapshot.toObject(DetailBookingCarUser::class.java)}"
                )
                val dataDetailBookingCar = snapshot.toObject(DetailBookingCarUser::class.java)
                bookingCarDetail.postValue(dataDetailBookingCar)
            }
        }
    }

    fun dataDetailBookingCar(): LiveData<DetailBookingCarUser> = bookingCarDetail

    fun getDataCar(carId: String?) {
        val carDb = db.collection("cars").document("$carId")
        carDb.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("$TAG get data car", "${exception.message}")
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("$TAG get data car", "${snapshot.toObject(Car::class.java)}")
                val carData = snapshot.toObject(Car::class.java)
                car.postValue(carData)
            }
        }
    }

    fun dataCar(): LiveData<Car> = car

    fun getDetailBookingTour(bookingId: String?) {
        val userDb = db.collection("users").document("$userId")
            .collection("bookingTour").document("$bookingId")

        userDb.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("$TAG getDetailBookingTour", "Error: ${exception.message}")
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("$TAG getDetailBookingTour", "$snapshot")
                val data = snapshot.toObject(DetailBookingTourUser::class.java)
                bookingTourDetail.postValue(data)
            }
        }
    }

    fun dataDetailBookingTour(): LiveData<DetailBookingTourUser> = bookingTourDetail

    fun getDataTour(tourId: String?) {
        val tourDb = db.collection("tour").document("$tourId")
        tourDb.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("$TAG getTour", "Error: ${exception.message}")
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("$TAG getTour", "$snapshot")
                val data = snapshot.toObject(Tour::class.java)
                tour.postValue(data)
            }
        }
    }

    fun dataTour(): LiveData<Tour> = tour

}