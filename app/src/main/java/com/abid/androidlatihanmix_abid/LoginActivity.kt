package com.abid.androidlatihanmix_abid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 7
    //sign in client
    private lateinit var mGoogleSignIn: GoogleSignInClient
    //firebase auth
    private lateinit var fAuth: FirebaseAuth
    private lateinit var btn_register: Button
    private lateinit var btn_login: Button
    private lateinit var et_email: EditText
    private lateinit var et_password: EditText
//    val sharedPreference : Preferences = Preferences(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login = findViewById(R.id.btn_login)
        btn_register = findViewById(R.id.btn_register)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_passowrd)

        fAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignIn = GoogleSignIn.getClient(this, gso)
        btn_login.setOnClickListener {
            signIn()
        }

        btn_login.setOnClickListener {
            if (et_email.length() == 0) {
                et_email.error = "Email harus diisi!"
            } else if (et_password.length() == 0) {
                et_password.error = "Password harus diisi!"
            } else {
                val intent = Intent(this, MainActivity::class.java)
                Preferences(this).setStatusInput(true)
                Preferences(this).setEmail(et_email.text.toString())
                startActivity(intent)
            }
        }

        btn_login_google.setOnClickListener {
            signIn()
        }

        btn_register.setOnClickListener {
            val intent = Intent(this, Daftar::class.java)
            startActivity(intent)
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignIn.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d("FAUTH_LOGIN", "firebaseAuth : ${account.id}")

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        fAuth.signInWithCredential(credential).addOnCompleteListener(this) {
            if (it.isSuccessful) {
//                Toast.makeText(this,"Login Berhasil, welcome ${fAuth.currentUser!!.displayName}",
//                    Toast.LENGTH_SHORT).show()
                val user = fAuth.currentUser
                updateUi(user)
            } else {
                Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(
                this, "Login Berhasil, welcome ${user.displayName}",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.e("AUTH LOGIN", "Login Gagal")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = fAuth.currentUser
        if (fAuth.currentUser != null) {
            updateUi(user)
//            Toast.makeText(this, "welcome ${fAuth.currentUser!!.displayName}",
//                Toast.LENGTH_SHORT).show()
        }
    }
}
