package com.azhara.perintisadventure.ui.home.ui.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.azhara.perintisadventure.R
import com.bumptech.glide.Glide
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import java.io.ByteArrayOutputStream

/**
 * A simple [Fragment] subclass.
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class EditProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var profileViewModel: ProfileViewModel
    private var imgUri: Uri? = null
    private var bitmapImage: Bitmap? = null
    private var requestImg = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ProfileViewModel::class.java]
        loading(true)

        btn_change_password.setOnClickListener(this)
        btn_save_edt_profile.setOnClickListener(this)
        add_img.setOnClickListener(this)

        profileViewModel.getData()
        setDataUser()
        statusEditMessage()
    }

    // Set new and old data user to edittext
    private fun setDataUser() {
        profileViewModel.dataUser().observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                edt_name.setText(data.name)
                edt_email.setText(data.email)
                edt_phone.setText(data.phone)
                activity?.let { Glide.with(it).load("${data.imgUrl}").into(img_profile) }
                loading(false)
            } else {
                context?.let {
                    Toasty.error(it, "Data tidak dapat dimuat!", Toast.LENGTH_LONG, true).show()
                }
            }
        })
    }

    // update data and edittext handler
    private fun updateData() {
        loading(true)
        val name = edt_name.text.toString().trim()
        val email = edt_email.text.toString().trim()
        val phone = edt_phone.text.toString().trim()

        if (name.isEmpty()) {
            edt_name.error = "Name tidak boleh kosong!"
        }
        if (email.isEmpty()) {
            edt_email.error = "Email tidak boleh kosong!"
        }
        if (phone.isEmpty()) {
            edt_phone.error = "Phone tidak boleh kosong!"
        }
        if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
            //profileViewModel.editDataUser(name, email, phone)
            if (imgUri != null) {
                profileViewModel.editDataAndImgUser(imageByteArray(bitmapImage), name, email, phone)
            } else {
                profileViewModel.editDataUserWithOutPhoto(name, email, phone)
            }
        }
    }

    // State from edit output true/false true=success, false=error
    private fun statusEditMessage() {
        profileViewModel.editMessage().observe(viewLifecycleOwner, Observer { msg ->
            loading(false)
            if (msg == "Data berhasil di update") {
                context?.let { Toasty.success(it, msg, Toast.LENGTH_LONG, true).show() }
            } else {
                context?.let { Toasty.error(it, msg, Toast.LENGTH_LONG, true).show() }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_change_password -> {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_user_edit_profile_to_navigation_change_password)
            }
            R.id.btn_save_edt_profile -> {
                btn_save_edt_profile.isEnabled = false
                updateData()
            }
            R.id.add_img -> {
                openGallery()
            }
        }
    }

    // State Loading
    private fun loading(state: Boolean) {
        if (state) {
            loading_background_edit_profile.visibility = View.VISIBLE
            loading_edit_profile.visibility = View.VISIBLE
            loading_edit_profile.playAnimation()
        } else {
            loading_background_edit_profile.visibility = View.INVISIBLE
            loading_edit_profile.visibility = View.INVISIBLE
            loading_edit_profile.cancelAnimation()
            btn_save_edt_profile.isEnabled = true
        }
    }

    private fun imageByteArray(bitmap: Bitmap?): ByteArray {
        val bitmapImage = bitmap //get file bitmap
        val bitmapCompress = bitmapImage?.let { resizeBitmap(it, 200) } //resize bitmap file
        val baos = ByteArrayOutputStream()
        bitmapCompress?.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            baos
        ) //compress bitmap extension to JPEG
        val data = baos.toByteArray() //compress to byteArray

        return data
    }

    // resize filebitmap with specific size
    private fun resizeBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width //get width image
        var height = image.height //get heigh image

        val bitMapRatio = width.toFloat() / height.toFloat()
        if (bitMapRatio > 1) {
            width = maxSize
            height = (width / bitMapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitMapRatio).toInt()
        }

        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    // Intent open gallery
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestImg)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == requestImg && data != null && data.data != null) {
            imgUri = data.data!!
            activity?.let { Glide.with(it).load(imgUri).into(img_profile) }

            if (imgUri != null) {
                try {
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(activity?.contentResolver, imgUri)
                        Log.d("EditProfileFragment", "bitmap android sdk < 28 $bitmap")
                        bitmapImage = bitmap

                    } else {
                        val source =
                            activity?.contentResolver?.let {
                                ImageDecoder.createSource(
                                    it,
                                    imgUri!!
                                )
                            }
                        val bitmap = source?.let { ImageDecoder.decodeBitmap(it) }
                        Log.d("EditProfileFragment", "bitmap android sdk 28 $bitmap")
                        bitmapImage = bitmap
                    }
                } catch (e: Exception) {
                    Log.e("EditProfileFragment", "${e.message}")
                }
            }

        }
    }

    // Get File extension photo
//    private fun getFileExtension(uri: Uri): String?{
//        val cr = activity?.contentResolver
//        val mimemap =MimeTypeMap.getSingleton()
//        return mimemap.getExtensionFromMimeType(cr?.getType(uri))
//    }

    // Upload test
//    private fun uploadTest(bitmap: Bitmap){
//        val bitmapImage = bitmap
//        val bitmapCompress = resizeBitmap(bitmapImage, 100)
//        val baos = ByteArrayOutputStream()
//        bitmapCompress.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val data = baos.toByteArray()
//        val mountainsRef = FirebaseStorage.getInstance().reference.child("test").child("aas")
//        var uploadTask = mountainsRef.putBytes(data)
//        uploadTask.addOnFailureListener {
//            Log.e("EditProfileFragment", "Failed upload")
//        }.addOnSuccessListener {
//            Log.d("EditProfileFragment", "Success upload")
//        }
//
//    }

}
