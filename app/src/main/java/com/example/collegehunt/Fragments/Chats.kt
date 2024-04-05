package com.example.collegehunt.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.collegehunt.R
import com.example.collegehunt.chatmsg
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.message.view.*


class Chats : AppCompatActivity() {
    private lateinit var homeview: View
    lateinit var ref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chats)
listenmsg()
        chatsshow.adapter = adpater
    }

    class Msg(val chatmsg: chatmsg): Item<GroupieViewHolder>()
    {
        override fun bind(viewHolder: GroupieViewHolder, position: Int)
        {
viewHolder.itemView.msgshow.text = chatmsg.id        }

        override fun getLayout(): Int
        {
            return R.layout.message
        }
    }
    val adpater = GroupAdapter<GroupieViewHolder>()
    private fun listenmsg(){
        val fromID = FirebaseAuth.getInstance().currentUser!!.uid

        val ref =  FirebaseDatabase.getInstance().getReference("/messages/$fromID")
        ref.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
           val chat = snapshot.getValue(chatmsg::class.java)
                Log.d("INTO", "djkhfiuerfh${chat!!.id}")
                adpater.add(Msg(chat))
        /*     snapshot.children.forEach{
             //    Log.d("INTO",it.toString())
             }
          */  }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
        })  }

}
