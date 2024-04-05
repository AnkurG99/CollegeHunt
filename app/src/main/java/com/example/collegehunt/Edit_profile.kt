package com.example.collegehunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.ageGet
import kotlinx.android.synthetic.main.activity_edit_profile.city
import kotlinx.android.synthetic.main.activity_edit_profile.email
import kotlinx.android.synthetic.main.activity_edit_profile.firstName
import kotlinx.android.synthetic.main.activity_edit_profile.interest
import kotlinx.android.synthetic.main.activity_edit_profile.lastName
import kotlinx.android.synthetic.main.activity_edit_profile.mobile

class Edit_profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)


        buttoncon.setOnClickListener{

           uploadtodatabase()
            val acc = Intent(this, Dashboard::class.java)
            startActivity(acc)
            finish()
        }
        val user  = FirebaseAuth.getInstance().currentUser!!.uid

        val reff = FirebaseDatabase.getInstance().reference.child("/Students").child(user)
        reff.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val info = snapshot.child("/email").value
                val first = snapshot.child("/firstname").value
                val last = snapshot.child("/lastname").value
                val m = snapshot.child("/number").value
                val uri = snapshot.child("/profileimageurl").value
                val age = snapshot.child("/age").value
                val inr = snapshot.child("/interesr").value
                val cit = snapshot.child("/city").value
                
                
         

                mobile.setText(m.toString())
                firstName.setText(first.toString())
                lastName.setText(last.toString())
                ageGet.setText(age.toString())
                email.setText(info.toString())
                Picasso.get().load(uri.toString()).placeholder(R.drawable.account).error(R.mipmap.ic_launcher).into(updatephoto)
                interest.setText(inr.toString())
                city.setText(cit.toString())

            }
        })

}
/*    private var selectedPhotoUris: Uri? = null
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
    }*/
  private fun uploadtodatabase(){

        val uid = FirebaseAuth.getInstance().currentUser?.uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/Students/$uid")

    ref.child("/firstname").setValue(firstName.text.toString())
    ref.child("/lastname").setValue(lastName.text.toString())
    ref.child("/age").setValue(ageGet.text.toString())
    ref.child("/interesr").setValue(interest.text.toString())
    ref.child("/city").setValue(city.text.toString())
    ref.child("/number").setValue(mobile.text.toString())


        .addOnSuccessListener {
                Toast.makeText(this, "DATA UPDATED", Toast.LENGTH_LONG).show()
                Log.d("Main", "Fianlly save a users")
            }
            .addOnFailureListener{
                Log.d("Main", "Message ${it.message}")
            }

    }


}