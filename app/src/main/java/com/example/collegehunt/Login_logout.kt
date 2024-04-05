package com.example.collegehunt

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login_logout.*

class  Login_logout : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_logout)


        textView21.setOnClickListener {
            val build =   AlertDialog.Builder(this).setTitle("Reset Password")
                .setMessage("Enter email to get reset password link")

            val v = layoutInflater.inflate(R.layout.dialog_reset_password, null)
            val id = v.findViewById<EditText>(R.id.dialog)

            build.setView(v)
            build.setPositiveButton("Reset", DialogInterface.OnClickListener { dialog, which -> Int

                forgotpassword(id)
                dialog.dismiss()
            })
            build.setNegativeButton("close", DialogInterface.OnClickListener { dialog, which -> Int
                dialog.dismiss()
            }).create().show()
        }

        textView10.setOnClickListener {
    val log = Intent(this, register::class.java)
    startActivity(log)
    finish()
}
        loginButton2.setOnClickListener{
            if(emaillogin2.text.toString().isEmpty()){
                emaillogin2.error = "Enter Email"
            }else if(!Patterns.EMAIL_ADDRESS.matcher(emaillogin2.text.toString()).matches()){
                emaillogin2.error = "Enter Valid Email"
            }
            else if(loginPassword2.text.toString().isEmpty()){
                loginPassword2.error = "Enter password"
            }else{
            FirebaseAuth.getInstance().signInWithEmailAndPassword(emaillogin2.text.toString(),loginPassword2.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    if(FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
                        val dash = Intent(this, Dashboard::class.java)
                        dash.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(dash)
                        finish()


                        sharedPreferences = getSharedPreferences("MYPREF", Activity.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("Email",emaillogin2.text.toString())
                        editor.putString("Pass", loginPassword2.text.toString())
                        editor.putBoolean("Key_Name", true)

                        editor.commit()


                    }}
            }.addOnFailureListener {
                Toast.makeText(this, "${it.message}",Toast.LENGTH_LONG).show()
            }

        }
        }
    }


    fun forgotpassword(id: EditText){
        if (id.text.toString().isEmpty()) {
            id.error = "Enter Email"
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(id.text.toString()).matches()) {
            id.error = "Enter Valid Email"

        }
            FirebaseAuth.getInstance().sendPasswordResetEmail(id.text.toString())
                .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "Check your mail for reset link", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{
                Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
