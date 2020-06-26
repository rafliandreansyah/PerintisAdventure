package com.azhara.perintisadventure.ui.home.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HomeViewModel : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val userData = MutableLiveData<User>()
    private val className = HomeViewModel::class.java.simpleName
    var errorMessage: String? = null

    fun loadDataUser() {
        val user = auth.currentUser
        val docRef = user?.uid?.let { db.collection("users").document(it) }
        docRef?.get()?.addOnSuccessListener { document ->
            val docUser = document.toObject(User::class.java)
            userData.postValue(docUser)
            Log.d(className, "${docUser}")
        }?.addOnFailureListener { exception ->
            Log.e(className, exception.message)
            errorMessage = exception.message
        }
    }

    fun loadUserDoc(): LiveData<User> = userData

}