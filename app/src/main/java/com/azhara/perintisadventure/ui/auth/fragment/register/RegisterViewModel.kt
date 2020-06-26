package com.azhara.perintisadventure.ui.auth.fragment.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var registerState = MutableLiveData<Boolean>()
    private val className = RegisterViewModel::class.java.simpleName
    var errorMessage: String? = null

    fun register(email: String, name: String, telephone: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val userData = User(user?.email, telephone, null, name)
                user?.uid?.let {
                    db.collection("users").document(it).set(userData).addOnSuccessListener {
                        registerState.postValue(true)
                    }.addOnFailureListener { exception ->
                        registerState.postValue(false)
                        Log.e(className, "Menambah data firestore error", exception)
                        user.delete().addOnCompleteListener {
                            if (task.isSuccessful) {
                                Log.d(
                                    className,
                                    "User dihapus karena firestore gagal menambah data user!"
                                )
                            }
                        }
                    }
                }

            } else {
                registerState.postValue(false)
                Log.e(className, task.exception?.message, task.exception)
                if (task.exception?.message == "The email address is badly formatted.") {
                    errorMessage = "Format email salah!"
                } else if (task.exception?.message == "A network error (such as timeout, interrupted connection or unreachable host) has occurred.") {
                    errorMessage = "Terjadi kesalahan pada jaringan!"
                } else {
                    errorMessage = task.exception?.message
                }
            }
        }
    }

    fun stateRegister(): LiveData<Boolean> {
        return registerState
    }

}