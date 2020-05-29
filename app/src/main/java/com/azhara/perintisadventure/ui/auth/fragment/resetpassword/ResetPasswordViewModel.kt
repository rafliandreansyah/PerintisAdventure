package com.azhara.perintisadventure.ui.auth.fragment.resetpassword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordViewModel: ViewModel(){

    private val auth = FirebaseAuth.getInstance()
    private val stateReset = MutableLiveData<Boolean>()
    var ErrorMessage: String? = null

    fun resetPassword(email: String){
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d("ResetPasswordViewModel", "Kirim Email Reset Berhasil")
                stateReset.postValue(true)
            }else{
                Log.e("ResetPasswordViewModel", task.exception?.message, task.exception)
                stateReset.postValue(false)
                ErrorMessage = task.exception?.message
            }
        }
    }

    fun resetPasswordState():LiveData<Boolean>{
        return stateReset
    }
}