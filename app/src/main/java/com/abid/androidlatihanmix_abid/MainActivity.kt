package com.abid.androidlatihanmix_abid

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.FrameLayout
import com.abid.androidlatihanmix_abid.adapter.BukuAdapter
import com.abid.androidlatihanmix_abid.model.BukuModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BukuAdapter.FirebaseDataListener {
    private var bukuAdapter: BukuAdapter? = null
    private var rcView: RecyclerView? = null
    private var list: MutableList<BukuModel>? = null
    lateinit var dbref: DatabaseReference
    lateinit var helperPref: Preferences
    private lateinit var fAuth: FirebaseAuth

    override fun onDeleteData(buku: BukuModel, position: Int) {

    }

    override fun onUpdateData(buku: BukuModel, position: Int) {

    }

    val manager = supportFragmentManager


    private var content: FrameLayout? = null

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.navigation1 -> {
                    val fragment = HomeFragment()
                    addFragment(fragment)
                    return true
                }
                R.id.navigation2 -> {
                    val fragment2 = UploadFragment()
                    addFragment(fragment2)
                    return true
                }
                R.id.navigation3 -> {
                    val fragment3 = ProfileFragment()
                    addFragment(fragment3)
                    return true
                }
            }
            return false
        }

    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.content, fragment)
            .commit()
    }

    /**
     * add/replace fragment in container [framelayout]
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val fragment = HomeFragment.newInstance()
        addFragment(fragment)
    }
}
