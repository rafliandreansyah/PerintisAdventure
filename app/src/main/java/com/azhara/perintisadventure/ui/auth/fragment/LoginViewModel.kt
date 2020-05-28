package com.azhara.perintisadventure.ui.auth.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel(){
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var status = MutableLiveData<Boolean>()

    fun login(email: String, password:String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
            if (task.isSuccessful){
                Log.d("LoginViewModel", "Login berhasil")
                status.postValue(true)

            }else{
                status.postValue(false)
            }
        }
    }



    fun loginStatus(): LiveData<Boolean>{
        return status
    }

    fun getSessionUsers(): FirebaseUser? {
       return auth.currentUser
    }

}