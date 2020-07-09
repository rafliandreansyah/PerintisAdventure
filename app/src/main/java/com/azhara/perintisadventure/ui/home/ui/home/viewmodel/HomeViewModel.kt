package com.azhara.perintisadventure.ui.home.ui.home.viewmodel

import android.transition.Slide
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.News
import com.azhara.perintisadventure.entity.Slider
import com.azhara.perintisadventure.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HomeViewModel : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val userData = MutableLiveData<User>()
    private val sliderData = MutableLiveData<List<Slider>>()
    private val dataNews = MutableLiveData<List<News>>()
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

    fun loadSlider(){
        val sliderDb = db.collection("slider").orderBy("sliderPosition")
        sliderDb.addSnapshotListener { value, error ->
            if (error != null){
                Log.e("HomeViewModel Slider", "Error: ${error.message}")
            }
            if (value != null){
                val data = value.toObjects(Slider::class.java)
                sliderData.postValue(data)
                Log.d("Data slider", "$data")
            }
        }
    }

    fun dataSlider(): LiveData<List<Slider>> = sliderData

    fun loadNews(){
        val newsDb = db.collection("news").orderBy("date", Query.Direction.DESCENDING).limit(6)
        newsDb.addSnapshotListener { value, error ->
            if (error != null){
                Log.e("LoadnewsHome", "Error: ${error.message}")
            }

            if (value != null){
                val data = value.toObjects(News::class.java)
                dataNews.postValue(data)
                Log.d("NewsData", "$data")
            }
        }
    }

    fun dataNews(): LiveData<List<News>> = dataNews

}