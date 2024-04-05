package com.example.collegehunt

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Landing : AppCompatActivity() {
private lateinit var sharedPreferences: SharedPreferences

    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)


        handler = Handler()
        handler.postDelayed({

        }, 3000)

        sharedPreferences = this.getSharedPreferences("MYPREF", Context.MODE_PRIVATE)
        val choice: Boolean = sharedPreferences.getBoolean("Key_Name", false)
        val em: String? = sharedPreferences.getString("Email", null)

        if (em == null) {
            val reg = Intent(this, register::class.java)
            startActivity(reg)
            finish()
        } else {
            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(em.toString())
                .addOnCompleteListener {

                    val check: Boolean = it.result?.signInMethods!!.isEmpty()
                    if (check) {

                        val reg = Intent(this, register::class.java)
                        reg.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        reg.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(reg)
                        finish()
                    } else {
                        if (choice == true) {
                            val dash = Intent(this, Dashboard::class.java)
                            dash.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            dash.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(dash)
                            finish()
                        } else {
                            val reg = Intent(this, register::class.java)
                            reg.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            reg.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(reg)
                            finish()
                        }
                    }
                }.addOnFailureListener {
                val reg2 = Intent(this, register::class.java)
                reg2.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                reg2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(reg2)
                finish()
            }

        }

    }

    }




