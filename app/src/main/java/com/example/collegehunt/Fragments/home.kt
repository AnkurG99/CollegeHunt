package com.example.collegehunt.Fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.collegehunt.*

import com.example.collegehunt.R
import com.firebase.ui.auth.util.ui.BucketedTextChangeListener

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.card.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.microedition.khronos.egl.EGLDisplay
import android.content.Intent as Intent1

// Search basis on keywords
// Refresh list on interest and filters
// Add more filters

class home : Fragment() {
    private lateinit var collegelist: RecyclerView
    lateinit var ref: DatabaseReference
    lateinit var sv: EditText
    lateinit var arr: ArrayList<college>
    private lateinit var homeview: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeview = inflater.inflate(R.layout.fragment_home, container, false)
arr = ArrayList<college>()
        collegelist = homeview.findViewById(R.id.collegeshow)
        collegelist.layoutManager
        collegelist.addItemDecoration(
            DividerItemDecoration(
                this.activity,
                DividerItemDecoration.VERTICAL
            )
        )

        sv = homeview.findViewById(R.id.searchView)
sv.addTextChangedListener(object : TextWatcher{
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val text = sv.text.toString()
        search(text)
    }

    override fun afterTextChanged(s: Editable?) {
val texta = sv.text.toString()
    search(texta)}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }
})



    return homeview


}


    fun search(text : String){
    ref = FirebaseDatabase.getInstance().getReference("/College")

val query = ref.orderByChild("/search").startAt(text).endAt(text+"\uf8ff")

    ref = FirebaseDatabase.getInstance().getReference("/College")
//initialize recycler view


    val option = FirebaseRecyclerOptions.Builder<college>()
        .setQuery(query,college::class.java).build()
//adapter
    val FirebaseRecyclerAdapter = object :  FirebaseRecyclerAdapter<college, CustomViewHodler>(option){
        //onCreate
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHodler {
            val layoutInflater = LayoutInflater.from(parent.context)
            val cellforrow = layoutInflater.inflate(R.layout.card, parent, false)
            return CustomViewHodler(cellforrow)
        }


        //onBind
        override fun onBindViewHolder(holder: CustomViewHodler, position: Int, model: college) {



            val refid = getRef(position).key.toString()
            ref.child(refid).addValueEventListener(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{
                        Log.d("Message", it.toString())
                    }

                    holder.itemView.setOnClickListener{
                        val user_id = getRef(position).key
                        val cp = Intent1(context, collegeInfo::class.java)
                        cp.putExtra("UID",user_id)
                        startActivity(cp)
                    }
                    holder.username.text = model.firstname
                    holder.uid.text= model.email
                    Picasso.get().load(model.profileimageurl).placeholder(R.drawable.ic_launcher_background).error(
                        R.drawable.ic_launcher_foreground).into(holder.image)
                }


            })
        }


    }
    //assigning adapter to recycler view
    this.collegelist.adapter = FirebaseRecyclerAdapter
    FirebaseRecyclerAdapter.startListening()




    //ViewHolder class
    class CustomViewHodler(v: View) :RecyclerView.ViewHolder(v){
        var username :TextView = v.findViewById(R.id.textView2)
        var uid :TextView = v.findViewById(R.id.textView3)
        var image : ImageView = v.findViewById(R.id.imageView)
    }
}

    override fun onStart() {
        super.onStart()

        ref = FirebaseDatabase.getInstance().getReference("/College")
//initialize recycler view


        val option = FirebaseRecyclerOptions.Builder<college>()
            .setQuery(ref,college::class.java).build()
//adapter
        val FirebaseRecyclerAdapter = object :  FirebaseRecyclerAdapter<college, CustomViewHodler>(option){
            //onCreate
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHodler {
                val layoutInflater = LayoutInflater.from(parent.context)
                val cellforrow = layoutInflater.inflate(R.layout.card, parent, false)
                return CustomViewHodler(cellforrow)
            }


            //onBind
            override fun onBindViewHolder(holder: CustomViewHodler, position: Int, model: college) {



                val refid = getRef(position).key.toString()
                ref.child(refid).addValueEventListener(object: ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach{
                            Log.d("Message", it.toString())

                        }

                        holder.itemView.setOnClickListener{
                            val user_id = getRef(position).key
                            Log.d("TRY", user_id.toString())
                         val cp = Intent1(context, collegeInfo::class.java)
                            cp.putExtra("UID",user_id)
                            startActivity(cp)
                        }
                        holder.username.text = model.firstname
                        holder.uid.text= model.tagline
                        holder.lastname.text = model.lastname
                        Picasso.get().load(model.profileimageurl).placeholder(R.drawable.ic_launcher_background).error(
                            R.drawable.ic_launcher_foreground).into(holder.image)
                    }


                })
            }


        }
        //assigning adapter to recycler view
        this.collegelist.adapter = FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter.startListening()



    }
    //ViewHolder class
    class CustomViewHodler(v: View) :RecyclerView.ViewHolder(v){
        var username :TextView = v.findViewById(R.id.textView2)
        var uid :TextView = v.findViewById(R.id.textView3)
var lastname :TextView = v.findViewById(R.id.textView20)
        var image : ImageView = v.findViewById(R.id.imageView)
    }



}




