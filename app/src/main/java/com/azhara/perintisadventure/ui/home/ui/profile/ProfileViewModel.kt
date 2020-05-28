package com.azhara.perintisadventure.ui.home.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileViewModel : ViewModel() {

   private val firebaseAuth = FirebaseAuth.getInstance()

    fun signOut(){
        firebaseAuth.signOut()
    }
}