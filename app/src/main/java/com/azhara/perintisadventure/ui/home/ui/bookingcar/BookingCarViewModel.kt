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

class BookingCarViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance()
    private val dataCar = MutableLiveData<List<Car>>()
    private val partner = MutableLiveData<Partner>()
    private val area = MutableLiveData<List<PickUpArea>>()
    private val checkBooking = MutableLiveData<Boolean>()
    private val bookingListId = MutableLiveData<String>()
    private val TAG = BookingCarViewModel::class.java.simpleName

    fun getDataCar() {
        val loadDb = db.collection("cars").whereEqualTo("statusReady", true)
//        loadDb.get().addOnSuccessListener { documentSnapshot->
//            val carsData = documentSnapshot.toObjects(Car::class.java)
//            Log.d(TAG, "$carsData")
//            dataCar.postValue(carsData)
//        }
        loadDb.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.w(TAG, exception.message)
            }
            if (snapshot != null) {
                val updateCarsData = ArrayList<Car>()
                val doc = snapshot.documents
                doc.forEach {
                    val carUpdate = it.toObject(Car::class.java)
                    if (carUpdate != null) {
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

    fun getDataPartner(id: String) {
        val loadDbPartner = db.collection("partners").document(id)
        loadDbPartner.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.w(TAG, exception.message)
            }
            if (snapshot != null && snapshot.exists()) {
                val updateDataPartner = snapshot.toObject(Partner::class.java)
                Log.d("$TAG update data partner", "$updateDataPartner")
                partner.postValue(updateDataPartner)
            }
        }
    }

    fun dataPartner(): LiveData<Partner> = partner

    fun getDataPickUpArea() {
        val loadArea = db.collection("pickup_location")
        loadArea.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.w(TAG, exception.message)
            }
            if (snapshot != null) {
                val setDataArea = snapshot.toObjects(PickUpArea::class.java)
                area.postValue(setDataArea)
            }
        }
    }

    fun dataArea(): LiveData<List<PickUpArea>> = area

    fun booking(
        partnerId: String?, carId: String?, totalPrice: Long?, startDate: Timestamp?,
        endDate: Timestamp?, driver: String?, pickUpArea: String?, carName: String?
    ) {
        val userId = user.currentUser?.uid
        val dataBookingForUser = DetailBookingCarUser(
            partnerId,
            carId,
            totalPrice,
            startDate,
            endDate,
            driver,
            pickUpArea,
            0 // 0 On progress dan 1 Selesai
        )

        val userBookingCarDb = db.collection("users").document("$userId")
            .collection("bookingCar")

        userBookingCarDb.add(dataBookingForUser).addOnSuccessListener {
            Log.d("$TAG Set bookingCar User", "Berhasil ditambah")
            Log.d("$TAG bookingId", it.id)

            userListBooking(userId, it.id, carName, totalPrice, startDate)
            bookingDataPartner(
                partnerId,
                userId,
                carId,
                it.id,
                totalPrice,
                startDate,
                endDate,
                driver,
                pickUpArea
            )
            updateDataBookedCar(userId, startDate, endDate, carId)

        }.addOnFailureListener {
            checkBooking.postValue(false)
            Log.w("$TAG Set bookingCar User", "Gagal ditambah ${it.message}")
        }

    }

    private fun userListBooking(
        userId: String?,
        bookingId: String?,
        bookingName: String?,
        totalPrice: Long?,
        startDate: Timestamp?
    ) {
        val bookingList = BookingList(
            bookingId,
            bookingName,
            totalPrice,
            startDate,
            imgUrlProofPayment = null,
            bookingType = 0, // 0 Tipe Booking Mobil dan 1 Tipe Booking Wisata
            statusPayment = false,
            uploadProofPayment = false
        )

        val userListBooking = db.collection("users").document("$userId")
            .collection("listBooking")

        userListBooking.add(bookingList).addOnSuccessListener {
            val idList = it.id
            bookingListId.postValue(idList)
            Log.d("$TAG Set listBooking User", "Berhasil ditambah")
            Log.d("$TAG Set listBooking User", "Id: $idList")

        }.addOnFailureListener {
            Log.w("$TAG Set listBooking User", "Gagal ditambah ${it.message}")
        }
    }

    fun bookingListId(): LiveData<String> = bookingListId

    private fun bookingDataPartner(
        partnerId: String?, userId: String?, carId: String?, bookingId: String,
        totalPrice: Long?, startDate: Timestamp?, endDate: Timestamp?, driver: String?,
        pickUpArea: String?
    ) {
        val dataBookingPartner = BookingCar(
            userId,
            carId,
            bookingId,
            totalPrice,
            startDate,
            endDate,
            driver,
            pickUpArea,
            false,
            0,
            false,
            null
        )

        val partner = db.collection("partners").document("$partnerId")
            .collection("bookingCar")

        partner.add(dataBookingPartner).addOnSuccessListener {
            Log.d("$TAG Set bookingCar partner", "Berhasil ditambah")


        }.addOnFailureListener {
            checkBooking.postValue(false)
            Log.w("$TAG Set bookingCar partner", "Gagal ditambah ${it.message}")
        }
    }

    private fun updateDataBookedCar(
        userId: String?,
        startDate: Timestamp?,
        endDate: Timestamp?,
        carId: String?
    ) {
        val updateBookedCar = BookedDate(userId, startDate, endDate)

        val carDb = db.collection("cars").document("$carId")
        carDb.update("booked", FieldValue.arrayUnion(updateBookedCar)).addOnSuccessListener {
            Log.d("$TAG update booked car", "Berhasil diupdate")
            checkBooking.postValue(true)
        }.addOnFailureListener { e ->
            checkBooking.postValue(false)
            Log.d("$TAG update booked car", "Gagal diupdate ${e.message}")
        }
    }

    fun checkBooking(): LiveData<Boolean> = checkBooking
}