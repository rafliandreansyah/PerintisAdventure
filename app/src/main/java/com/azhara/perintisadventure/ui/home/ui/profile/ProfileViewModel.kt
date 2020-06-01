package com.azhara.perintisadventure.ui.home.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.Users
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

class ProfileViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db =  FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val dataUserProfile = MutableLiveData<Users>()
    private val user = firebaseAuth.currentUser

    private val editState = MutableLiveData<Boolean>()
    private val changePasswordState = MutableLiveData<Boolean>()
    private val getDataState = MutableLiveData<Boolean>()

    var errorMessage:String? = null

    private var storageTask: StorageTask<UploadTask.TaskSnapshot>? = null

    private val className = ProfileViewModel::class.java.simpleName

    // Get data user from firebase
    fun getData(){
        user?.uid?.let {
            db.collection("users").document(it).get().addOnSuccessListener { doc ->
                if (doc != null){
                    val data = doc.toObject(Users::class.java)
                    dataUserProfile.postValue(data)
                    getDataState.postValue(true)
                }
            }.addOnFailureListener { exception ->
                Log.e(className, exception.message)
                getDataState.postValue(false)
                if (exception.message == "A network error (such as timeout, interrupted connection or unreachable host) has occurred."){
                    errorMessage = "Kesalahan jaringan, silahkan cek jaringan anda!"
                }else{
                    errorMessage = exception.message
                }
            }

        }
    }
    // Value of data user (ProfileFragment)
    fun dataUser(): LiveData<Users> = dataUserProfile

    // Edit data user firebase
    private fun editDataUser(name: String?, email:String?, phone:String?, urlImg: String?){
        val data = hashMapOf<String?, Any?>(
            "name" to name,
            "email" to email,
            "phone" to phone,
            "imgUrl" to urlImg
        )
        user?.uid?.let {
            db.collection("users").document(it)
                .update(data).addOnSuccessListener {
                    Log.d(className, "Data Update")
                    editState.postValue(true)
                }.addOnFailureListener { e ->
                    editState.postValue(false)
                    if (e.message == "A network error (such as timeout, interrupted connection or unreachable host) has occurred."){
                        errorMessage = "Kesalahan jaringan, silahkan cek jaringan anda!"
                    }else{
                        errorMessage = e.message
                    }
                }
        }
    }

    fun editDataUserWithOutPhoto(name: String?, email:String?, phone:String?){
        val data = hashMapOf<String?, Any?>(
            "name" to name,
            "email" to email,
            "phone" to phone
        )
        user?.uid?.let {
            db.collection("users").document(it)
                .update(data).addOnSuccessListener {
                    Log.d(className, "Data Update")
                    editState.postValue(true)
                }.addOnFailureListener { e ->
                    editState.postValue(false)
                    if (e.message == "A network error (such as timeout, interrupted connection or unreachable host) has occurred."){
                        errorMessage = "Kesalahan jaringan, silahkan cek jaringan anda!"
                    }else{
                        errorMessage = e.message
                    }
                }
        }
    }

    // change password firebase (ChangePasswordFragment)
    fun changePassword(oldPassword: String, newPassword:String){
        val credential = user?.email?.let { EmailAuthProvider.getCredential(it, oldPassword) }
        if (credential != null) {
            user?.reauthenticate(credential)?.addOnSuccessListener {
                Log.d(className, "Success authenticated")
                user.updatePassword(newPassword).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Log.d(className, "Success Update password")
                        changePasswordState.postValue(true)
                    }else{
                        Log.e(className, task.exception?.message)
                        changePasswordState.postValue(false)
                    }
                }

            }?.addOnFailureListener { e ->
                Log.e(className, e.message)
                if (e.message == "The password is invalid or the user does not have a password.") {
                    errorMessage = "Password lama salah"
                } else if (e.message == "A network error (such as timeout, interrupted connection or unreachable host) has occurred.") {
                    errorMessage = "Kesalahan jaringan, silahkan cek jaringan anda!"
                } else {
                    errorMessage = e.message
                }
            }
        }

    }
    //Edit data user function firbase (EditProfileFragment)
    fun editDataAndImgUser(byteArrayImg: ByteArray, name: String?, email:String?, phone:String?){
        val imgStorage = user?.uid?.let { storage.reference.child("users")
            .child(it).child(user.uid) }
        if (imgStorage != null){
            imgStorage.delete()
        }
        storageTask = imgStorage?.putBytes(byteArrayImg) //Upload with putBytes
        Log.d("byteArrayImg", "$byteArrayImg")
        storageTask?.addOnFailureListener { e ->
            Log.e(className, "Upload failed")
            if (e.message == "A network error (such as timeout, interrupted connection or unreachable host) has occurred."){
                errorMessage = "Kesalahan jaringan, silahkan cek jaringan anda!"
            }else{
                errorMessage = e.message
            }
        }?.addOnSuccessListener {
            (storageTask as UploadTask).continueWithTask{ task ->
                if (!task.isSuccessful){
                    Log.e(className, task.exception?.message)
                }
                imgStorage?.downloadUrl
            }.addOnCompleteListener {task ->
                if(task.isSuccessful){
                    val urlImg = task.result
                    editDataUser(name, email, phone, urlImg.toString())
                }else{
                    Log.d(className, "Upload img failed")
                    if (task.exception?.message == "A network error (such as timeout, interrupted connection or unreachable host) has occurred."){
                        errorMessage = "Kesalahan jaringan, silahkan cek jaringan anda!"
                    }else{
                        errorMessage = task.exception?.message
                    }
                }
            }
        }
    }

    fun editState(): LiveData<Boolean> = editState

    fun changePasswordState(): LiveData<Boolean> = changePasswordState

    fun getDataState(): LiveData<Boolean> = getDataState

    // sign out (ProfileFragment)
    fun signOut(){
        firebaseAuth.signOut()
    }
}