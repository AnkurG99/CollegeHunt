package com.example.collegehunt

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_college_profile.*
import java.util.jar.Manifest
private val CODE = 1
class collegeProfile : AppCompatActivity() {
    lateinit var courses_college: RecyclerView
    lateinit var getuid : String
    lateinit var ref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_college_profile)
        courses_college = findViewById(R.id.courses_college)
getuid = intent.extras?.get("UID").toString()
        Log.d("UID2", getuid)

         ref = FirebaseDatabase.getInstance().getReference("/College").child(getuid).child("/courses")
//initialize recycler view

  ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
               snapshot.children.forEach{
                   Log.d("CourseCheck", it.toString())

               }
            }
       })
        val option = FirebaseRecyclerOptions.Builder<modelCourse>()
            .setQuery(ref, modelCourse::class.java).build()
//adapter
        val FirebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<modelCourse, CustomViewHodler>(option) {
                //onCreate
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): CustomViewHodler {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val cellforrow = layoutInflater.inflate(R.layout.course_card, parent, false)
                    return CustomViewHodler(cellforrow)
                }


                //onBind
                override fun onBindViewHolder(
                    holder: CustomViewHodler,
                    position: Int,
                    model: modelCourse
                ) {
                    val refid = getRef(position).key.toString()
                    ref.child(refid).addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {

                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.children.forEach {
                                Log.d("Message", it.toString())
                            }

                            holder.username.text = model.id

                        }

                    })
                }


            }
        //assigning adapter to recycler view
        this.courses_college.adapter = FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter.startListening()





       getuid = intent.extras?.get("UID").toString()

        Log.d("uid", getuid)
textView28.setOnClickListener {
    if(expand.visibility == View.GONE){
        TransitionManager.beginDelayedTransition(cd3, AutoTransition())
        expand.visibility = View.VISIBLE
        textView28.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))

    }else{
        TransitionManager.beginDelayedTransition(cd3, AutoTransition())
        expand.visibility = View.GONE
        textView28.setTextColor(ContextCompat.getColor(this, R.color.bulletproof))


    }
}


        val reff = FirebaseDatabase.getInstance().getReference().child("/College").child(getuid)
        reff.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val info = snapshot.child("/email").value
                val first = snapshot.child("/firstname").value
                val last = snapshot.child("/lastname").value
                val m = snapshot.child("/number").value
                val Uri = snapshot.child("/profileimageurl").value
                val web = snapshot.child("/age").value


                website.setOnClickListener{
                    goto(web.toString())
                }
        call.setOnClickListener{
           if(ContextCompat.checkSelfPermission(this@collegeProfile,
               android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
           {

            goton(m.toString())
        }
        else{
requestp()
           }
        }
fs.setText(first.toString())
                ls.setText(last.toString())
                emailc.setText(info.toString())
                num.setText(m.toString())
                Picasso.get().load(Uri.toString()).placeholder(R.drawable.logonew).into(getpic)

            }            }
        )
    }


   private fun requestp(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)){
            AlertDialog.Builder(this).setTitle("Permission Needed")
                .setMessage("Phone permission in required to make call from this app")
                .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which -> Int

                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), CODE )
                })
                .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which -> Int
                dialog.dismiss()}).create().show()
        }
        else{
ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), CODE )
        }
    }
  private  fun goto(s:String){
        val uri = Uri.parse(s)
        startActivity(Intent (Intent.ACTION_VIEW,uri))

    }
    private fun goton(n:String){
        val m = "tel:"+ n
        val urin = Uri.parse(m)
        startActivity(Intent (Intent.ACTION_CALL,urin))


    }

    class CustomViewHodler(v: View) : RecyclerView.ViewHolder(v){
        var username : TextView = v.findViewById(R.id.textView17)

    }


}
