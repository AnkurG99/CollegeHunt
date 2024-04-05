package com.example.collegehunt.Fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.collegehunt.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_account.*


class Account : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var edit : FloatingActionButton
lateinit var email: TextView
    lateinit var fn: TextView
    lateinit var ln: TextView
    lateinit var number: TextView
    lateinit var logout : TextView
    lateinit var pimage : ImageView
    private lateinit var homeview: View
    lateinit var inter :TextView
    lateinit var cit : TextView
    lateinit var coverimage: ImageView
    lateinit var f:ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):

            View? {
        // Inflate the layout for this fragment
        homeview = inflater.inflate(R.layout.fragment_account, container, false)
logout = homeview.findViewById(R.id.logout)
        email = homeview.findViewById(R.id.textView6)
fn = homeview.findViewById(R.id.textView7)
        pimage=homeview.findViewById(R.id.selectphoto)
        ln = homeview.findViewById(R.id.textView8)
        inter = homeview.findViewById(R.id.textView13)
        number = homeview.findViewById(R.id.textView9)
edit = homeview.findViewById(R.id.floatingActionButton)
        f= homeview.findViewById(R.id.imageView6)

cit = homeview.findViewById(R.id.textView14)
        coverimage = homeview.findViewById(R.id.imageView6)
edit.setOnClickListener {

    val ep = Intent(this.activity, Edit_profile::class.java)
    startActivity(ep)
    val users  = FirebaseAuth.getInstance().currentUser!!.uid

}
        f.setOnClickListener{
            val x = resources.displayMetrics.density
            Log.d("TAG",x.toString())
            val f = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
            if(f == Configuration.SCREENLAYOUT_SIZE_SMALL)
                Log.d("TRIALF", "SMALL")
            else if(f == Configuration.SCREENLAYOUT_SIZE_NORMAL)
                Log.d("TRIALF", "NORMAL")
            else if(f == Configuration.SCREENLAYOUT_SIZE_LARGE)
                Log.d("TRIALF", "LARGE")
            else if(f == Configuration.SCREENLAYOUT_SIZE_XLARGE)
                Log.d("TRIALF", "XLARGE")
            else
                Log.d("TRIALF", "XXLARGE")


        }

        val user  = FirebaseAuth.getInstance().currentUser!!.uid


        val reff = FirebaseDatabase.getInstance().getReference().child("/Students").child(user)
        reff.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val info = snapshot.child("/email").value
                val first = snapshot.child("/firstname").value
                val last = snapshot.child("/lastname").value
                val m = snapshot.child("/number").value
                val Uri = snapshot.child("/profileimageurl").value
                val inte = snapshot.child("interesr").value
                val loc = snapshot.child("/city").value
                val cover = snapshot.child("/coverimage").value

                /*       snapshot.children.forEach{
                    Log.d("INFO", it.toString())

                }
*/if(m.toString().isEmpty()){

                    email.text = info.toString()
                    fn.text = first.toString()
                    ln.text= last.toString()
                    number.text = "##########"
                    inter.text = inte.toString()
                    cit.text = loc.toString()
                    Picasso.get().load(Uri.toString()).placeholder(R.drawable.logonew).into(pimage)
                    Picasso.get().load(cover.toString()).into(coverimage)

                }else{

          email.text = info.toString()
                fn.text = first.toString()
                ln.text= last.toString()
                number.text = m.toString()
                inter.text = inte.toString()
                cit.text = loc.toString()
              Picasso.get().load(Uri.toString()).placeholder(R.drawable.logonew).into(pimage)
                    Picasso.get().load(cover.toString()).into(coverimage)

                }
            }

        }


        )




        logout.setOnClickListener {
    FirebaseAuth.getInstance().signOut()
    sharedPreferences = this.activity!!.getSharedPreferences("MYPREF", Activity.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    editor.putBoolean("Key_Name", false)

    editor.apply()

    val login = Intent(this.activity, Login_logout::class.java)
    startActivity(login)
    activity!!.finish()

        }


        return homeview

    }
    private fun privateAccount(){
    //Not visible
    // email
    // phone number
    // Interest 

    }
    private fun publicAccount(){
// everything will be visible
        // Phone number still not visible
        // Interest if user want them to show
    }


}
