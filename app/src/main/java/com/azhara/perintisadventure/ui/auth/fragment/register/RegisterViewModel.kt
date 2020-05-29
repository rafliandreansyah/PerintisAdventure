package com.azhara.perintisadventure.ui.auth.fragment.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel(){

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var registerState = MutableLiveData<Boolean>()

    fun register(email: String, name: String, telephone: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(){task ->
            if (task.isSuccessful){
                val user = auth.currentUser
                val userData = Users(user?.email, telephone, null, name)
                user?.uid?.let { db.collection("users").document(it).set(userData).addOnSuccessListener {
                    registerState.postValue(true)
                }.addOnFailureListener {exception ->
                    registerState.postValue(false)
                    Log.e("RegisterViewModel", "Menambah data firestore error", exception)
                }}

            }else{
                registerState.postValue(false)
                Log.e("RegisterViewModel", "Registrasi User Error",task.exception)
            }
        }
    }

    fun stateRegister(): LiveData<Boolean>{
        return registerState
    }

}