package com.abid.androidlatihanmix_abid


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileFragment : Fragment() {

    private lateinit var fAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fAuth = FirebaseAuth.getInstance()
        val tv_email = view.findViewById<TextView>(R.id.tv_email)

        tv_email.text = "${fAuth.currentUser!!.displayName}"

        btn_logout.setOnClickListener {
            fAuth.signOut()
            Preferences(activity!!).setStatusInput(false)
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}