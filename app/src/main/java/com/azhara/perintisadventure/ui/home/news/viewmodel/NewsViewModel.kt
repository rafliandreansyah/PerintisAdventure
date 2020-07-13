package com.azhara.perintisadventure.ui.home.news.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.News
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NewsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val news = MutableLiveData<List<News>>()

    fun getNews() {
        val newsDb = db.collection("news").orderBy("date", Query.Direction.DESCENDING)
        newsDb.addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("GetListNews", "Error: ${error.message}")
            }

            if (value != null) {
                val data = value.toObjects(News::class.java)
                news.postValue(data)
                Log.d("GetListNews", "$data")
            }
        }
    }

    fun datanNews(): LiveData<List<News>> = news
}