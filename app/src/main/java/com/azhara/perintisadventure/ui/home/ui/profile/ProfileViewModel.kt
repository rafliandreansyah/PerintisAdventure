package com.azhara.perintisadventure.ui.home.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadventure.entity.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ProfileViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db =  FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val dataUserProfile = MutableLiveData<User>()
    private val user = firebaseAuth.currentUser

    private val editMsg = MutableLiveData<String>()
    private val changePasswordMsg = MutableLiveData<String>()

    private var storageTask: StorageTask<UploadTask.TaskSnapshot>? = null

    private val className = ProfileViewModel::class.java.simpleName

    // Get data user from firebase
    fun getData(){
        user?.uid?.let {
            db.collection("users").document(it).get().addOnSuccessListener { doc ->
                if (doc != null){
                    val data = doc.toObject(User::class.java)
                    dataUserProfile.postValue(data)
                }
            }.addOnFailureListener { exception ->
                Log.e(className, "email: ${user.email} ${exception.message}")
            }

        }
    }
    // Value of data user (ProfileFragment)
    fun dataUser(): LiveData<User> = dataUserProfile

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
                    Log.d(className, "Email: ${user.email} Data Update")
                    editMsg.postValue("Data berhasil di update")
                }.addOnFailureListener { e ->
                    Log.e(className, "email: ${user.email}, editDataUser, ${e.message}")
                    if (e.message == "A network error (such as timeout, interrupted connection or unreachable host) has occurred."){
                        editMsg.postValue("Kesalahan jaringan, silahkan cek jaringan anda!")
                    }else{
                        editMsg.postValue(e.message)
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
                    editMsg.postValue("Data berhasil di update")
                }.addOnFailureListener { e ->
                    Log.e(className, "email: ${user.email}, editDataUserWithOutPhoto, exception: ${e.message}")
                    if (e.message == "A network error (such as timeout, interrupted connection or unreachable host) has occurred."){
                        editMsg.postValue("Kesalahan jaringan, silahkan cek jaringan anda!")
                    }else{
                        editMsg.postValue(e.message)
                    }
                }
        }
    }

    // change password firebase (ChangePasswordFragment)
    fun changePassword(oldPassword: String, newPassword:String){
        val credential = user?.email?.let { EmailAuthProvider.getCredential(it, oldPassword) }
        if (credential != null) {
            user?.reauthenticate(credential)?.addOnSuccessListener {
                Log.d(className, "email: ${user.email} Success authenticated")
                user.updatePassword(newPassword).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Log.d(className, "email:${user.email} Success Update password")
                        changePasswordMsg.postValue("Password berhasil di update")
                    }else{
                        Log.e(className, "email: ${user.email}, changePassword, exception: ${task.exception?.message}")
                        changePasswordMsg.postValue(task.exception?.message)
                    }
                }

            }?.addOnFailureListener { e ->
                Log.e(className, "email: ${user.email}, changePassword, exception: ${e.message}")
                if (e.message == "The password is invalid or the user does not have a password.") {
                    changePasswordMsg.postValue("Password lama salah")
                } else if (e.message == "A network error (such as timeout, interrupted connection or unreachable host) has occurred.") {
                    changePasswordMsg.postValue("Kesalahan jaringan, silahkan cek jaringan anda!")
                } else {
                    changePasswordMsg.postValue(e.message)
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
            Log.e(className, "${user?.email} ${e.message}")
            if (e.message == "A network error (such as timeout, interrupted connection or unreachable host) has occurred."){
                editMsg.postValue("Kesalahan jaringan, silahkan cek jaringan anda!")
            }else{
                editMsg.postValue(e.message)
            }
        }?.addOnSuccessListener {
            (storageTask as UploadTask).continueWithTask{ task ->
                if (!task.isSuccessful){
                    Log.e(className, "email: ${user?.email}, editDataAndImgUser, exception: ${task.exception?.message}")
                    editMsg.postValue(task.exception?.message)
                }
                imgStorage?.downloadUrl
            }.addOnCompleteListener {task ->
                if(task.isSuccessful){
                    val urlImg = task.result
                    editDataUser(name, email, phone, urlImg.toString())
                }else{
                    Log.d(className, "${user?.email} ${task.exception?.message}")
                    if (task.exception?.message == "A network error (such as timeout, interrupted connection or unreachable host) has occurred."){
                        editMsg.postValue("Kesalahan jaringan, silahkan cek jaringan anda!")
                    }else{
                        editMsg.postValue(task.exception?.message)
                    }
                }
            }
        }
    }

    fun editMessage(): LiveData<String> = editMsg
    fun changePassMessage(): LiveData<String> = changePasswordMsg

    // sign out (ProfileFragment)
    fun signOut(){
        firebaseAuth.signOut()
    }
}