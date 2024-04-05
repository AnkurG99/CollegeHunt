package com.example.collegehunt
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

var emailch = "emial"

class Login : AppCompatActivity()
{

   private lateinit var sharedPreferences: SharedPreferences


    lateinit var uid: String

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //reset password button
        resetPasswordButton.setOnClickListener{
            val build = AlertDialog.Builder(this).setTitle("Reset Password")
                        .setMessage("Enter email to get reset password link")

            val viewResetPassword = layoutInflater.inflate(R.layout.dialog_reset_password, null)
            val id = viewResetPassword.findViewById<EditText>(R.id.dialog)

            build.setView(viewResetPassword)
            build.setPositiveButton(resources.getString(R.string.reset), DialogInterface.OnClickListener { dialog, which -> Int
                forgotPassword(id)
            })
            build.setNegativeButton("close", DialogInterface.OnClickListener { dialog, which -> Int
            dialog.dismiss()
            }).create().show()
        }
        //reset password button End
        textView.setOnClickListener {
        val reg = Intent(this, register::class.java)
        startActivity(reg)
        finish()
        }
        button7.setOnClickListener {
            passwordUpdate()
        }
        loginButton.setOnClickListener {
            if (emaillogin.text.toString().isEmpty())
            {
                emaillogin.error = "Enter Email"
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(emaillogin.text.toString()).matches())
            {
                emaillogin.error = "Enter Valid Email"
            }
            else if (loginPassword.text.toString().isEmpty())
            {
                loginPassword.error = "Enter Password"
            }
            else
            {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emaillogin.text.toString(),
                    loginPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful)
                        {
                            if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified)
                                {
                                    uploadToDB()
                                    val dashboardIntent = Intent(this, makeProfile::class.java)
                                    dashboardIntent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(dashboardIntent)
                                        finish()
                                    // Set shared preference
                                    emailch = emaillogin.text.toString()
                                    sharedPreferences = getSharedPreferences("MYPREF", Activity.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.putString("Email", emaillogin.text.toString())
                                    editor.putString("Pass", loginPassword.text.toString())
                                    editor.putBoolean("Key_Name", true)
                                    editor.apply()
                                    // Set shared preference End
                                }
                                else
                                {
                                    Toast.makeText(this, "Verify Email", Toast.LENGTH_LONG).show()
                                }
                        }
                    }.addOnFailureListener{
                        Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
                    }

            }

        }
    }
    private fun passwordUpdate()
    {
        val pass = loginPassword.text.toString()
        FirebaseAuth.getInstance().currentUser?.updatePassword(pass)
        button7.text = "DONE"
    }
    private fun forgotPassword(id: EditText)
    {
        if (id.text.toString().isEmpty())
            {
                id.error = "Enter Email"
            }
        if(!Patterns.EMAIL_ADDRESS.matcher(id.text.toString()).matches())
        {
            id.error = "Enter Valid Email"
            FirebaseAuth.getInstance().sendPasswordResetEmail(id.text.toString()).addOnCompleteListener {
            if(it.isSuccessful)
            {
                Toast.makeText(this, "Check your mail for reset link", Toast.LENGTH_LONG).show()
            }
            }
        }
    }
    private fun uploadToDB()
    {
        uid = FirebaseAuth.getInstance().currentUser?.uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/Students/$uid")
        val user = Email(emaillogin.text.toString())
        ref.setValue(user)
        .addOnSuccessListener {
            Log.d("Main", "Fianlly save a users")
        }
        .addOnFailureListener{
            Log.d("Main", "Message ${it.message}")
        }
    }



}
    class Email(val email:String)
    {
        constructor(): this("")
    }