package com.example.collegehunt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit
// Email link authentication for ome time
// Email and password for next login
// Phone authentication
// Merge sign in option
class register : AppCompatActivity() {
    companion object {
        private const val TAG = "PasswordlessSignIn"
        private const val KEY_PENDING_EMAIL = "key_pending_email"
    }
    lateinit var test: ConstraintLayout

    /*NEED TO BE DELETED
    password
    signIn Button*/
    private var pendingEmail: String=""
    private var emailLink: String=""
    //Firebase auth
    private lateinit var auth : FirebaseAuth
    private var storedVerificationId : String? = null
    private var resendToken : PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //Initializing auth
        auth = FirebaseAuth.getInstance()
      // Click Listeners
        /*signIn.setOnClickListener{
            val ll = Intent(this, Login_logout::class.java)
            startActivity(ll)
            finish()}*/
        button4.setOnClickListener {
            val mat = resources.displayMetrics.density
            Log.d("CHECKED", mat.toString())
            onSendLinkClicked()
        }
        button6.setOnClickListener {
            onSignInClicked()
        }
        textView22.setOnClickListener {
            val builder = AlertDialog.Builder(this).setTitle("Conditions For Password")
            val view = layoutInflater.inflate(R.layout.conditions,null)
            builder.setView(view).create().show()
        }
        registerButton.setOnClickListener{
          //  performRegister()
        }
        nlog.setOnClickListener{
            performNumber()
        }
        if(savedInstanceState != null){
            pendingEmail = savedInstanceState.getString(KEY_PENDING_EMAIL, null)
            emailGet.setText(pendingEmail)
        }
        //function call
        checkIntent(intent)

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                /*This callback will be invoked in two situations:
                 1 - Instant verification. In some cases the phone number can be instantly
                     verified without needing to send or enter a verification code.
                 2 - Auto-retrieval. On some devices Google Play services can automatically
                     detect the incoming verification SMS and perform verification without
                     user action.*/
                Log.d("MAIN", "onVerificationCompleted:$credential")

                signInWithPhoneAuthCredential(credential)
                Toast.makeText(this@register,"SUCESS",Toast.LENGTH_LONG).show()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("MAIN", "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(this@register, "WORNG", Toast.LENGTH_LONG).show()

                } else if (e is FirebaseTooManyRequestsException) {
                      Toast.makeText(this@register, "The SMS quota for the project has been exceeded", Toast.LENGTH_LONG).show()
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("MAIN", "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
            }
        }
        /*
               }*/
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_PENDING_EMAIL, pendingEmail)
    }
    private fun checkIntent(intent: Intent?) {
        if (intentHasEmailLink(intent)) {
            emailLink = intent!!.data!!.toString()

            status.setText("LINK FOUND")
            button4.isEnabled = false
            button6.isEnabled = true
        } else {
            status.setText("NOT SENT")
          button4.isEnabled = true
            button6.isEnabled = false
        }
    }
    private fun intentHasEmailLink(intent: Intent?): Boolean {
        if (intent != null && intent.data != null) {
          val  intentData = intent.data!!.toString()
            if (auth.isSignInWithEmailLink(intentData)) {
                return true

            }
        }

        return false
    }
    private fun sendSignInLink(email: String) {
        val settings = ActionCodeSettings.newBuilder()
            .setAndroidPackageName(
                packageName,
                false, null/* minimum app version */)/* install if not available? */
            .setHandleCodeInApp(true)
            .setUrl("https://college-hunt-aab15.firebaseapp.com" +
                    "/emailSignInLink")
            .build()



        auth.sendSignInLinkToEmail(email, settings)
            .addOnCompleteListener { task ->


                if (task.isSuccessful) {
                    Log.d(TAG, "Link sent")
                    showSnackbar("Sign-in link sent!")

                    pendingEmail = email
                    status.setText("EMAIL SENT")
                } else {
                    val e = task.exception
                    Log.w(TAG, "Could not send link", e)
                    showSnackbar("Failed to send link.")

                    if (e is FirebaseAuthInvalidCredentialsException) {
                      emailGet.error = "Invalid email address."
                    }
                }
            }
    }
    private fun signInWithEmailLink(email: String, link: String?) {
        Log.d(TAG, "signInWithLink:" + link!!)




        auth.signInWithEmailLink(email, link)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmailLink:success")

                    emailGet.text = null

                    updateUI(task.result?.user)
                    val getlog = Intent(this, Login::class.java)
                    startActivity(getlog)
                } else {
                    Log.w(TAG, "signInWithEmailLink:failure", task.exception)
                    updateUI(null)

                    if (task.exception is FirebaseAuthActionCodeException) {
                        showSnackbar("Invalid or expired sign-in link.")
                    }
                }
            }
    }

    private fun onSendLinkClicked() {
        val email = emailGet.text.toString()
        if (TextUtils.isEmpty(email)) {
            emailGet.error = "Email must not be empty."
            return
        }

        sendSignInLink(email)
    }

    private fun onSignInClicked() {
        val email = emailGet.text.toString()
        if (TextUtils.isEmpty(email)) {
            emailGet.error = "Email must not be empty."
            return
        }

        signInWithEmailLink(email, emailLink)
        }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
          status.text = user.email
           button4.visibility = View.GONE
            button6.visibility = View.VISIBLE
        } else {
           button4.visibility = View.VISIBLE
            button6.visibility = View.GONE
        }
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential).addOnSuccessListener {
Toast.makeText(this, "SUCCESS", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this, "FAILED", Toast.LENGTH_LONG).show()
        }
    }
private fun performNumber(){
    val num = numloog.text.toString()
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(num)       // Phone number to verify
        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
        .setActivity(this)                 // Activity (for callback binding)
        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        .build()
    PhoneAuthProvider.verifyPhoneNumber(options)


}

   /* private fun performRegister(){
        val email = emailGet.text.toString()
        val pass = password.text.toString()
     if (email.isEmpty()){
         emailGet.error = "Enter Email"
     }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
         emailGet.error = "Enter Valid Email"
     }
     else if(pass.isEmpty()){
         password.error = "Enter Password"

     }else{
         if(pass.length < 8){
             password.error = "Minimum Length 8"
         }else {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener {
                if (!it.isSuccessful)
                    return@addOnCompleteListener
                //else if success

                FirebaseAuth.getInstance().currentUser!!.sendEmailVerification()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Registered Successfully Please check your email for verification",
                                Toast.LENGTH_LONG
                            ).show()
                            val mp = Intent(this, Login::class.java)
                            mp.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(mp)
                            finish()
                        }
                    else
                        {
                            Toast.makeText(this,"!!FAILED!!",Toast.LENGTH_LONG).show()
                        }}
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
            }
     }
    }
 }*/

    }



