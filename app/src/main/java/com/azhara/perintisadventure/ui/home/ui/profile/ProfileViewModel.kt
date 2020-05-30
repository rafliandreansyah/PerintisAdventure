package com.azhara.perintisadventure.ui.home.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db =  FirebaseFirestore.getInstance()
    private val dataUser = MutableLiveData<Users>()
    private val userId = firebaseAuth.currentUser?.uid

    private val className = ProfileViewModel::class.java.simpleName

    fun getData(){
        if (userId != null) {
            db.collection("users").document(userId).get().addOnSuccessListener { doc ->
                if (doc != null){
                    val data = doc.toObject(Users::class.java)
                    dataUser.postValue(data)
                }
            }.addOnFailureListener { exception ->
                Log.e(className, exception.message)
            }
        }
    }
    fun dataUser(): LiveData<Users> = dataUser

    fun editDataUser(name: String, email:String, phone:String){
        val data = hashMapOf<String, Any>(
            "name" to name,
            "email" to email,
            "phone" to phone
        )
        if (userId != null) {
            db.collection("users").document(userId)
                .update(data).addOnSuccessListener {
                    Log.d(className, "Data Update")
                }.addOnFailureListener { e ->

                }
        }
    }

    fun signOut(){
        firebaseAuth.signOut()
    }
}