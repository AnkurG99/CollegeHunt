package com.example.collegehunt

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_college_info.*
import kotlinx.android.synthetic.main.activity_college_profile.*
import kotlinx.android.synthetic.main.activity_register.*
private val CODE = 1

class collegeInfo : AppCompatActivity() {
    lateinit var ref : DatabaseReference
    lateinit var getuid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_college_info)

        getuid = intent.extras?.get("UID").toString()

        val reff = FirebaseDatabase.getInstance().getReference().child("/College").child(getuid)
        reff.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val info = snapshot.child("/email").value
                val first = snapshot.child("/firstname").value
                val last = snapshot.child("/lastname").value
                val m = snapshot.child("/number").value
                val Uri = snapshot.child("/profileimageurl").value
                val web = snapshot.child("/web").value
                val desc = snapshot.child("/description").value
                val elg = snapshot.child("/eligibility").value
                textView35.setText("Minimum"+ ":" + " " + elg.toString()+ "%")
                textView80.setText(last.toString())
                textView29.setText(first.toString())
                textView81.setText(desc.toString())
                Picasso.get().load(Uri.toString()).into(imageView4)

supportActionBar?.title = first.toString() + " " + last.toString()

                imageButton2.setOnClickListener{
                    goto(web.toString())
                }
                imageButtoncall.setOnClickListener{
                    if(ContextCompat.checkSelfPermission(this@collegeInfo,
                            android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                    {

                        goton(m.toString())
                    }
                    else{
                        requestp()
                    }
                }
            }



        })

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
            object : FirebaseRecyclerAdapter<modelCourse, collegeProfile.CustomViewHodler>(option) {
                //onCreate
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): collegeProfile.CustomViewHodler {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val cellforrow = layoutInflater.inflate(R.layout.course_card, parent, false)
                    return collegeProfile.CustomViewHodler(cellforrow)
                }


                //onBind
                override fun onBindViewHolder(
                    holder: collegeProfile.CustomViewHodler,
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
        this.ghn.adapter = FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter.startListening()

    }
    private fun delete(id: String){
val refrem = FirebaseDatabase.getInstance().getReference("/College").child(id)
    refrem.removeValue()
    }

    private fun requestp() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CALL_PHONE
            )
        ) {
            AlertDialog.Builder(this).setTitle("Permission Needed")
                .setMessage("Phone permission in required to make call from this app")
                .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which ->
                    Int

                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.CALL_PHONE),
                        CODE
                    )
                })
                .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which ->
                    Int
                    dialog.dismiss()
                }).create().show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CALL_PHONE),
                CODE
            )
        }
    }

    private fun goto(s: String) {
        val uri = Uri.parse(s)
        startActivity(Intent(Intent.ACTION_VIEW, uri))

    }

    private fun goton(n: String) {
        val m = "tel:" + n
        val urin = Uri.parse(m)
        startActivity(Intent(Intent.ACTION_CALL, urin))


    }

    class CustomViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        var username: TextView = v.findViewById(R.id.textView17)

    }
}
