package com.azhara.perintisadventure.ui.auth.fragment.resetpassword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val stateReset = MutableLiveData<Boolean>()
    var errorMessage: String? = null
    private val className = ResetPasswordViewModel::class.java.simpleName

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(className, "Kirim Email Reset Berhasil")
                stateReset.postValue(true)
            } else {
                Log.e(className, task.exception?.message, task.exception)
                stateReset.postValue(false)
                if (task.exception?.message == "The email address is badly formatted.") {
                    errorMessage = "Format email salah!"
                } else if (task.exception?.message == "There is no user record corresponding to this identifier. The user may have been deleted.") {
                    errorMessage = "Email tidak terdaftar!"
                } else if (task.exception?.message == "A network error (such as timeout, interrupted connection or unreachable host) has occurred.") {
                    errorMessage = "Terjadi kesalahan pada jaringan!"
                } else {
                    errorMessage = task.exception?.message
                }

            }
        }
    }

    fun resetPasswordState(): LiveData<Boolean> {
        return stateReset
    }
}