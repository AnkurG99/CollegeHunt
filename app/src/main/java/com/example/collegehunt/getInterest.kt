 package com.example.collegehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegehunt.Fragments.test
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.test.*

 class getInterest : AppCompatActivity() {
     lateinit var ref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test)

      editmarks.addTextChangedListener(object : TextWatcher{

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = editmarks.text.toString()
                search(text)
            }

            override fun afterTextChanged(s: Editable?) {
                val texta = editmarks.text.toString()
                search(texta)}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })


    }
     private fun search(text : String){

ref = FirebaseDatabase.getInstance().getReference("/Courses/courses")
         val query2 = ref.orderByChild("/marks").endAt(text)

         ref = FirebaseDatabase.getInstance().getReference("/Courses/courses")
//initialize recycler view


         val option = FirebaseRecyclerOptions.Builder<modelCourse>()
             .setQuery(query2, modelCourse::class.java).build()
//adapter
         val FirebaseRecyclerAdapter =
             object : FirebaseRecyclerAdapter<modelCourse, test.CustomViewHodler>(option) {
                 //onCreate
                 override fun onCreateViewHolder(
                     parent: ViewGroup,
                     viewType: Int
                 ): test.CustomViewHodler {
                     val layoutInflater = LayoutInflater.from(parent.context)
                     val cellforrow = layoutInflater.inflate(R.layout.course_card, parent, false)
                     return test.CustomViewHodler(cellforrow)
                 }


                 //onBind
                 override fun onBindViewHolder(
                     holder: test.CustomViewHodler,
                     position: Int,
                     model: modelCourse
                 ) {
                     val refid = getRef(position).key.toString()
                     Log.d("COURSE", refid)
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
         this.elgshow.adapter = FirebaseRecyclerAdapter
         FirebaseRecyclerAdapter.startListening()



         //ViewHolder class
         class CustomViewHodler(v: View) : RecyclerView.ViewHolder(v) {
             var username: TextView = v.findViewById(R.id.textView17)


         }

     }
}