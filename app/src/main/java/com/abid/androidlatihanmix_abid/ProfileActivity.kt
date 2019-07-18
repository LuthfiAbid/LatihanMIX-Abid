package com.abid.androidlatihanmix_abid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {
    private lateinit var tv_email: TextView
    private lateinit var status: Preferences
    private lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        fAuth = FirebaseAuth.getInstance()
        val tv_email = findViewById<TextView>(R.id.tv_email)

        tv_email.text = "${fAuth.currentUser!!.displayName}"

        btn_logout.setOnClickListener {
            fAuth.signOut()
            finish()
            Preferences(this).setStatusInput(false)
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
