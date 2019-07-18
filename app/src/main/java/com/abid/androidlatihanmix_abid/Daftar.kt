package com.abid.androidlatihanmix_abid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_daftar.*

class Daftar : AppCompatActivity() {


    lateinit var fAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference
    lateinit var helperPref: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)
        fAuth = FirebaseAuth.getInstance()

        Signup.setOnClickListener {
            var email = et_email_daftar.text.toString()
            var password = et_password_daftar.text.toString()
            var nama = et_nama_daftar.text.toString()
            helperPref = Preferences(this)
            if (email.isNotEmpty() || password.isNotEmpty() || !email.equals("") || !password.equals("")
                || et_email_daftar.length() == 6 || et_password_daftar.length() == 6
            ) {
                fAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            simpanToFirebase(nama, email, password)
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

    fun simpanToFirebase(nama: String, email: String, password: String) {
        val uidUser = helperPref.getUID()
        val counterId = helperPref.getCounterId()
        dbRef = FirebaseDatabase.getInstance().getReference("dataUser/$uidUser/$counterId")
        dbRef.child("/id").setValue(uidUser)
        dbRef.child("/nama").setValue(nama)
        dbRef.child("/email").setValue(email)
        dbRef.child("/password").setValue(password)
        helperPref.saveCounterId(counterId + 1)
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
