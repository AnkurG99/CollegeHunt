/*package com.example.collegehunt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.example.collegehunt.email
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_make_profile.*
import kotlinx.android.synthetic.main.activity_profile_pic.*
import java.util.*

class profile_pic : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_pic)
button3.setOnClickListener{
    uploadimage()

    val dash = Intent(this, Dashboard::class.java)
    dash.flags =
        Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(dash)
    finish()
}
        button2.setOnClickListener {
            Log.d("Main", "Try to upload image")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }



    }

    private var selectedPhotoUris: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("Main", "Photo was selected")

            selectedPhotoUris = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUris)
            selectphoto.setImageBitmap(bitmap)
            button2.alpha = 0f
        }
    }
    private fun uploadimage(){


        if(selectedPhotoUris != null) {
            val filename = UUID.randomUUID()
            val ref = FirebaseStorage.getInstance().getReference("/images/${filename}")
            ref.putFile(selectedPhotoUris!!)
                .addOnSuccessListener { it ->
                    Log.d("Main", "Succesfully Uploaded image : ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {

                        Log.d("Main", "File Location $it")
                        uploadtodatabase(it.toString())

                    }
                }
        }
        else {
            val uri = Uri.parse("android.resource://$packageName/${R.drawable.logonew}")
            uploadtodatabase(profileimageUrl = uri.toString())

        }
    }
    private fun uploadtodatabase(profileimageUrl: String){
        val uid = FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/Students/$uid")
        val user = pic(uid,  firstName.text.toString() , lastName.text.toString(), email.text.toString(),mobile.text.toString() ,ageGet.text.toString(),profileimageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Main", "Fianlly save a pic")
            }
            .addOnFailureListener{
                Log.d("Main", "Message ${it.message}")
            }
    }

}


class pic (val uid: String , val firstname:String , val lastname: String ,val email:String, val number:String, val age:String, val profileimageUrl:String)*/