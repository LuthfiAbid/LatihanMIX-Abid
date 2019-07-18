package com.abid.androidlatihanmix_abid


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.abid.androidlatihanmix_abid.adapter.BukuAdapter
import com.abid.androidlatihanmix_abid.model.BukuModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment(), BukuAdapter.FirebaseDataListener {

    override fun onDeleteData(buku: BukuModel, position: Int) {
        dbref = FirebaseDatabase.getInstance().getReference("dataBuku/${helperPref.getUID()}")
        if (dbref != null) {
            dbref.child(buku.getKey()).removeValue().addOnSuccessListener {
                Toast.makeText(context, "Data berhasil dihapus!", Toast.LENGTH_SHORT).show()
                bukuAdapter!!.notifyDataSetChanged()
            }
        }
    }

    override fun onUpdateData(buku: BukuModel, position: Int) {
        var datax = buku.getKey()
        val intent = Intent(context, TambahData::class.java)
        intent.putExtra("kode", datax)
        startActivity(intent)
    }

    private var bukuAdapter: BukuAdapter? = null
    private var rcView: RecyclerView? = null
    private var list: MutableList<BukuModel>? = null
    lateinit var dbref: DatabaseReference
    lateinit var helperPref: Preferences
    private lateinit var fAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helperPref = Preferences(context!!)
        rcView = view.findViewById(R.id.rc_view)
        list = mutableListOf()
        rcView!!.layoutManager = LinearLayoutManager(context)
        rcView!!.setHasFixedSize(true)
        fAuth = FirebaseAuth.getInstance()

        dbref = FirebaseDatabase.getInstance().getReference("dataBuku/${helperPref.getUID()}")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("TAG_ERROR", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    list = ArrayList()
                    for (dataSnapshot in p0.children) {
                        val addDataAll = dataSnapshot.getValue(BukuModel::class.java)
                        addDataAll!!.setKey(dataSnapshot.key!!)
                        list!!.add(addDataAll)
                        bukuAdapter =
                            BukuAdapter(activity!!, list!!)
                    }
                }
                rcView!!.adapter = bukuAdapter
            }
        })
    }

    companion object {
        fun newInstance(): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


}
