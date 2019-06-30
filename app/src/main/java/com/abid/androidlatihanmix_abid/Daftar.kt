package com.abid.androidlatihanmix_abid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_daftar.*
import kotlinx.android.synthetic.main.activity_login.*

class Daftar : AppCompatActivity() {

    lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)
        fAuth = FirebaseAuth.getInstance()

        Signup.setOnClickListener {
            var email = et_email_daftar.text.toString()
            var password = et_password_daftar.text.toString()
            if (email.isNotEmpty() || password.isNotEmpty() || !email.equals("") || !password.equals("")
                || et_email.length() == 6 || et_passowrd.length() == 6
            ) {
                fAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Register Berhasil!", Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        } else {
                            Toast.makeText(this, "Value must be 6 or more digit!", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Email dan password harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
