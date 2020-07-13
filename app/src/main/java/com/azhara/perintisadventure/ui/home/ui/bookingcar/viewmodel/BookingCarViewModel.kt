package com.azhara.perintisadventure.ui.home.ui.bookingcar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class BookingCarViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance()
    private val dataCar = MutableLiveData<List<Car>>()
    private val partner = MutableLiveData<Partner>()
    private val area = MutableLiveData<List<PickUpArea>>()
    private val checkBooking = MutableLiveData<Boolean>()
    private val bookingListId = MutableLiveData<String>()
    private val dataDetailCar = MutableLiveData<Car>()
    private val TAG = BookingCarViewModel::class.java.simpleName
    var errorMessage: String? = null
    private val userId = user.currentUser?.uid

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

    fun getDataPickUpAreaWithDriver(){
        val areaDb = db.collection("pickup_location_driver")
        areaDb.addSnapshotListener{value, error ->
            if (error != null){
                Log.d(TAG, error.message)
            }

            if (value != null){
                val data = value.toObjects(PickUpArea::class.java)
                area.postValue(data)
            }
        }
    }

    fun dataArea(): LiveData<List<PickUpArea>> = area

    fun booking(
        partnerId: String?, carId: String?, totalPrice: Long?, startDate: Timestamp?,
        endDate: Timestamp?, driver: String?, pickUpArea: String?, carName: String?
    ) {
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


            updateDataBookedCar(startDate, endDate, carId, partnerId,
                it.id,
                totalPrice,
                driver,
                pickUpArea,
                carName)

        }.addOnFailureListener {
            checkBooking.postValue(false)
            errorMessage = it.message
            Log.w("$TAG Set bookingCar User", "Gagal ditambah ${it.message}")
        }

    }

    private fun updateDataBookedCar(
        startDate: Timestamp?,
        endDate: Timestamp?,
        carId: String?,
        partnerId: String?,
        bookingCarUserId: String,
        totalPrice: Long?, driver: String?,
        pickUpArea: String?, carName: String?
    ) {

        val updateBookedCar = BookedDate(userId, startDate, endDate)

        val carDb = db.collection("cars").document("$carId")
        carDb.update("booked", FieldValue.arrayUnion(updateBookedCar)).addOnSuccessListener {
            Log.d("$TAG update booked car", "Berhasil diupdate")
            bookingDataPartner(
                partnerId,
                carId,
                bookingCarUserId,
                totalPrice,
                startDate,
                endDate,
                driver,
                pickUpArea,
                carName
            )
        }.addOnFailureListener { e ->
            checkBooking.postValue(false)
            errorMessage = e.message
            Log.d("$TAG update booked car", "Gagal diupdate ${e.message}")
        }
    }

    private fun bookingDataPartner(
        partnerId: String?, carId: String?, bookingCarUserId: String,
        totalPrice: Long?, startDate: Timestamp?, endDate: Timestamp?, driver: String?,
        pickUpArea: String?, carName: String?
    ) {
        val dataBookingPartner = BookingCar(
            userId,
            carId,
            null,
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
            val idBookingPartner = it.id
            userListBooking(bookingCarUserId, carName, totalPrice, startDate, partnerId, idBookingPartner)

        }.addOnFailureListener {
            checkBooking.postValue(false)
            Log.w("$TAG Set bookingCar partner", "Gagal ditambah ${it.message}")
            errorMessage = it.message
        }
    }

    private fun userListBooking(
        bookingId: String?,
        bookingName: String?,
        totalPrice: Long?,
        startDate: Timestamp?,
        partnerId: String?,
        bookingDbPartnerId: String?
    ) {
        val bookingList = BookingList(
            null,
            bookingId,
            partnerId,
            bookingDbPartnerId,
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

            updateDataListIdUsers(partnerId, bookingDbPartnerId, idList)

        }.addOnFailureListener {
            Log.w("$TAG Set listBooking User", "Gagal ditambah ${it.message}")
            checkBooking.postValue(false)
            errorMessage = it.message
        }
    }

    private fun updateDataListIdUsers(partnerId: String?, bookingDbPartnerId: String?, idList: String?){
        val userDb = db.collection("users")
            .document("$userId").collection("listBooking").document("$idList")

        userDb.update("bookingListId", idList).addOnSuccessListener {
            getIdListUsersToPartner(partnerId, bookingDbPartnerId, idList)
        }.addOnFailureListener {
            checkBooking.postValue(false)
            errorMessage = it.message
        }
    }

    private fun getIdListUsersToPartner(partnerId: String?, bookingDbPartnerId: String?, idList: String?){
        val getListIdToUpdatePartnerBookingCar = db.collection("partners")
            .document("$partnerId").collection("bookingCar").document("$bookingDbPartnerId")

        getListIdToUpdatePartnerBookingCar.update("bookingListUserId", idList).addOnSuccessListener {
            Log.d("$TAG update getListId", "Berhasil di update")
            checkBooking.postValue(true)
        }.addOnFailureListener { error ->
            Log.e("$TAG update getListId", "Gagal")
            checkBooking.postValue(false)
            errorMessage = error.message
        }
    }

    fun bookingListId(): LiveData<String> = bookingListId

    fun checkBooking(): LiveData<Boolean> = checkBooking

    fun updateStatusReadyFalse(carId: String?){
        val carDb = db.collection("cars").document("$carId")
        carDb.update("statusReady", false).addOnSuccessListener {
            Log.d("status ready false", "Berhasil")
        }.addOnFailureListener {
            Log.d("Update status ready", "Error: ${it.message}")
        }
    }

    fun updateStatusReadyTrue(carId: String?){
        val carDb = db.collection("cars").document("$carId")
        carDb.update("statusReady", true).addOnSuccessListener {
            Log.d("status ready true", "Berhasil")
        }.addOnFailureListener {
            Log.d("Update status ready", "Error: ${it.message}")
        }
    }

    fun checkBookingCar(carId: String?){
        val carDb = db.collection("cars").document("$carId")
        carDb.addSnapshotListener { value, error ->
            if (error != null){
                Log.e("Error load data car", "${error.message}" )
            }

            if (value != null && value.exists()){
                val data = value.toObject(Car::class.java)
                dataDetailCar.postValue(data)
            }
        }
    }

    fun dataDetailCar(): LiveData<Car> = dataDetailCar
}