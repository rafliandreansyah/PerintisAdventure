package com.azhara.perintisadventure.ui.home.ui.bookingcar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class BookingCarViewModel : ViewModel(){

    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance()
    private val dataCar = MutableLiveData<List<Car>>()
    private val partner = MutableLiveData<Partner>()
    private val area = MutableLiveData<List<PickUpArea>>()
    private val TAG = BookingCarViewModel::class.java.simpleName

    fun getDataCar() {
        val loadDb = db.collection("cars").whereEqualTo("statusReady", true)
//        loadDb.get().addOnSuccessListener { documentSnapshot->
//            val carsData = documentSnapshot.toObjects(Car::class.java)
//            Log.d(TAG, "$carsData")
//            dataCar.postValue(carsData)
//        }
        loadDb.addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Log.w(TAG, exception.message)
            }
            if (snapshot != null){
                val updateCarsData = ArrayList<Car>()
                val doc = snapshot.documents
                doc.forEach {
                    val carUpdate = it.toObject(Car::class.java)
                    if (carUpdate != null){
                        carUpdate.carId = it.id
                        updateCarsData.add(carUpdate)
                    }
                }
                Log.d("$TAG snapshot", "$updateCarsData")
                dataCar.postValue(updateCarsData)
            }
        }
    }

    fun dataCar(): LiveData<List<Car>> = dataCar

    fun getDataPartner(id: String){
        val loadDbPartner = db.collection("partners").document(id)
        loadDbPartner.addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Log.w(TAG, exception.message)
            }
            if (snapshot != null && snapshot.exists()){
                val updateDataPartner = snapshot.toObject(Partner::class.java)
                Log.d("$TAG update data partner", "$updateDataPartner")
                partner.postValue(updateDataPartner)
            }
        }
    }

    fun dataPartner(): LiveData<Partner> = partner

    fun getDataPickUpArea(){
        val loadArea = db.collection("pickup_location")
        loadArea.addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Log.w(TAG, exception.message)
            }
            if (snapshot != null){
                val setDataArea = snapshot.toObjects(PickUpArea::class.java)
                area.postValue(setDataArea)
            }
        }
    }

    fun dataArea(): LiveData<List<PickUpArea>> = area

    fun booking(partnerId: String?, carId: String?, totalPrice: Long?, startDate: Timestamp?,
                endDate: Timestamp?, driver: String?, pickUpArea: String?){
        val userId = user.currentUser?.uid

        val addBookingCar = BookedDate(userId, startDate, endDate)
        val carDb = db.collection("cars").document("$carId")
        carDb.update("booked", FieldValue.arrayUnion(addBookingCar)).addOnSuccessListener {
            Log.d("$TAG set union array car", "Berhasil update mobil")
        }.addOnFailureListener { e ->
            Log.w("$TAG set union array car", e.message)
        }

        val booking = Booking(partnerId, userId, carId, totalPrice, startDate, endDate, driver,pickUpArea, false, 0)
        val userDb = db.collection("users").document("$userId")
            .collection("booking")
        userDb.add(booking).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d("$TAG set booking user", "Berhasil menambah data")
            }else{
                Log.w("$TAG set booking user", task.exception?.message)
            }
        }

        val partner = db.collection("partners").document("$partnerId")
            .collection("booking")
        partner.add(booking).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d("$TAG set booking partner", "Berhasil menambah data")
            }else{
                Log.w("$TAG set booking partner", task.exception?.message)
            }
        }

    }
}