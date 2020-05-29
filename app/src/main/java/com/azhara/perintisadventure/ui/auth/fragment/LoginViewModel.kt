package com.azhara.perintisadventure.ui.auth.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LoginViewModel : ViewModel(){
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var status = MutableLiveData<Boolean>()
    var errorMessage: String? = null
    private val className = LoginViewModel::class.java.simpleName

    fun login(email: String, password:String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
            if (task.isSuccessful){
                Log.d(className, "Login berhasil")
                status.postValue(true)

            }else{
                status.postValue(false)
                Log.e(className, task.exception?.message)
                if (task.exception?.message == "The email address is badly formatted."){
                    errorMessage =  "Format email salah!"
                }else if(task.exception?.message == "There is no user record corresponding to this identifier. The user may have been deleted."){
                    errorMessage = "Email tidak terdaftar, Silahkan daftar!"
                }else if(task.exception?.message == "The password is invalid or the user does not have a password."){
                    errorMessage = "Password salah!"
                }
                else if(task.exception?.message == "A network error (such as timeout, interrupted connection or unreachable host) has occurred."){
                    errorMessage = "Terjadi kesalahan pada jaringan!"
                }else{
                    errorMessage = task.exception?.message
                }
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