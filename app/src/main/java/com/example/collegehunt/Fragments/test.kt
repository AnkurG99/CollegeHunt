package com.example.collegehunt.Fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.collegehunt.*
import com.example.collegehunt.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class test : Fragment() {
    lateinit var homeview: View
    lateinit var ref: DatabaseReference
    lateinit var collegelist: RecyclerView
    lateinit var sv: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeview = inflater.inflate(R.layout.fragment_eligiblity, container, false)

        collegelist = homeview.findViewById(R.id.course)
        collegelist.addItemDecoration(
            DividerItemDecoration(
                this.activity,
                DividerItemDecoration.VERTICAL
            )
        )

        sv = homeview.findViewById(R.id.searchView2)
        sv.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = sv.text.toString()
                search(text)
            }

            override fun afterTextChanged(s: Editable?) {
                val texta = sv.text.toString()
                search(texta)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })

        return homeview
    }

    fun search(text: String) {

        val query = ref.orderByChild("/id").startAt(text).endAt(text + "\uf8ff")

        ref = FirebaseDatabase.getInstance().getReference("/Courses/courses")
//initialize recycler view


        val option = FirebaseRecyclerOptions.Builder<modelCourse>()
            .setQuery(query, modelCourse::class.java).build()
//adapter
        val FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<modelCourse, CustomViewHodler>(option) {
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
        this.collegelist.adapter = FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter.startListening()



    //ViewHolder class

}
override fun onStart() {
        super.onStart()

        ref = FirebaseDatabase.getInstance().getReference("/Courses/courses")
//initialize recycler view


        val option = FirebaseRecyclerOptions.Builder<modelCourse>()
            .setQuery(ref, modelCourse::class.java).build()
//adapter
        val FirebaseRecyclerAdapter = object :  FirebaseRecyclerAdapter<modelCourse, CustomViewHodler>(option){
            //onCreate
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHodler {
                val layoutInflater = LayoutInflater.from(parent.context)
                val cellforrow = layoutInflater.inflate(R.layout.course_card, parent, false)
                return CustomViewHodler(cellforrow)
            }


            //onBind
            override fun onBindViewHolder(holder: CustomViewHodler, position: Int, model: modelCourse) {
                val refid = getRef(position).key.toString()
                ref.child(refid).addValueEventListener(object: ValueEventListener {
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
        this.collegelist.adapter = FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter.startListening()

    }
    //ViewHolder class
    class CustomViewHodler(v: View) : RecyclerView.ViewHolder(v){
        var username : TextView = v.findViewById(R.id.textView17)

    }

}