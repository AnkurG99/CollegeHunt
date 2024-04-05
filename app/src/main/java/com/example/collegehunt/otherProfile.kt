package com.example.collegehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_college_profile.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_other_profile.*

class otherProfile : AppCompatActivity() {
    lateinit var coverimage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_profile)
coverimage = findViewById(R.id.imageView6)


        val getuid = intent.extras?.get("UID").toString()

        Log.d("uid","$getuid")



        val reff = FirebaseDatabase.getInstance().getReference().child("/Students").child(getuid)
        reff.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Modelusers::class.java)
             val info = snapshot.child("/email").value
                val first = snapshot.child("/firstname").value
                val last = snapshot.child("/lastname").value
                val m = snapshot.child("/number").value
                val Uri = snapshot.child("/profileimageurl").value
                val inte = snapshot.child("/interesr").value
                val loc = snapshot.child("/city").value
                val cover = snapshot.child("/coverimage").value


if(m.toString().isEmpty()){
    textView7.text = first.toString()
    textView8.setText(last.toString())
    textView6.setText(info.toString())
    textView9.setText("##########")
    textView13.setText(inte.toString())
    textView14.setText(loc.toString())
    Picasso.get().load(Uri.toString()).placeholder(R.drawable.logonew).into(getpics)
    Picasso.get().load(cover.toString()).into(coverimage)
}else {
    textView7.setText(first.toString())
    textView8.setText(last.toString())
    textView6.setText(info.toString())
    textView9.setText(m.toString())
    textView13.setText(inte.toString())
    textView14.setText(loc.toString())
    Picasso.get().load(Uri.toString()).placeholder(R.drawable.logonew).into(getpics)
    Picasso.get().load(cover.toString()).into(coverimage)


}
            }
        }
        )


    }
}