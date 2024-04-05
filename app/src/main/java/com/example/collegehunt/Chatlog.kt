 package com.example.collegehunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.collegehunt.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chatlog.*
import kotlinx.android.synthetic.main.activity_other_profile.*
import kotlinx.android.synthetic.main.chattoleft.view.*
import kotlinx.android.synthetic.main.chattoright.view.*

class Chatlog : AppCompatActivity() {
lateinit var chats : RecyclerView
    val adpater = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlog)
imageView5.setOnClickListener{

    val text = editTextTextPersonName.text.toString()
    if(text.isNotEmpty()&&text.isNotBlank()){
       // editTextTextPersonName.error = "Message Empmty"
     performsendmsg()}
}

        val uid = intent.extras?.get("UIDS").toString()
        textView32.setOnClickListener {
            val intent =  Intent(this, otherProfile::class.java)
           intent.putExtra("UID", uid)
            startActivity(intent)
        }
val ref = FirebaseDatabase.getInstance().getReference("/Students").child(uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("/firstname").value
                val last = snapshot.child("/lastname").value
                val show:String = name.toString() + " " + last.toString()
                val Uri = snapshot.child("/profileimageurl").value
                textView32.setText(show)
                Picasso.get().load(Uri.toString()).into(imageViewtop)

            }
        })
listenformsg()
        chats = findViewById(R.id.chat)

        chats.adapter = adpater

    }


    private fun listenformsg(){
        val fromID = FirebaseAuth.getInstance().currentUser!!.uid
        val uid = intent.extras?.get("UIDS").toString()

        val toID = uid
        val refmsg = FirebaseDatabase.getInstance().getReference("/messages/$fromID/$toID")
    refmsg.addChildEventListener(object : ChildEventListener{
        override fun onCancelled(error: DatabaseError) {}
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        val Chatmsg = snapshot.getValue(chatmsg::class.java)
if(Chatmsg !=null){
    Log.d("MSGTO", Chatmsg.text)
if(Chatmsg.fromID == FirebaseAuth.getInstance().currentUser!!.uid){
adpater.add(chattoitem(Chatmsg.text))
}else{
adpater.add(chatfromitem(Chatmsg.text))
}
}

        }
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
    })

    }



    private fun performsendmsg(){
val text = editTextTextPersonName.text.toString()
     /*   if(text.isEmpty()){
            editTextTextPersonName.error = "Message Empmty"
        }else{
*/

        val fromID = FirebaseAuth.getInstance().currentUser!!.uid
        val uid = intent.extras?.get("UIDS").toString()

        val toID = uid
        if(fromID == null)
            return
        val refff = FirebaseDatabase.getInstance().getReference("/messages/$fromID/$toID").push()
     val reffto = FirebaseDatabase.getInstance().getReference("/messages/$toID/$fromID").push()
        val chat = chatmsg(refff.key!!,text,fromID,toID,System.currentTimeMillis()/1000)
        refff.setValue(chat).addOnSuccessListener {
            Log.d("MSG", "CHAT SAVED: ${refff.key}")
            editTextTextPersonName.text.clear()
            chats.scrollToPosition(adpater.itemCount-1)
        }

        reffto.setValue(chat)
            
  //  }
    }

    class chatfromitem(val text : String) : Item<GroupieViewHolder>(){
        override fun getLayout(): Int {
return R.layout.chattoleft
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.textView30.text = text
        }

    }
    class chattoitem(val text : String): Item<GroupieViewHolder>(){
        override fun getLayout(): Int {
            return  R.layout.chattoright
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.textView31.text = text
        }

    }
}