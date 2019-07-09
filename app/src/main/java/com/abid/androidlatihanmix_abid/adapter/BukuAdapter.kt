package com.abid.androidlatihanmix_abid.adapter

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.abid.androidlatihanmix_abid.MainActivity
import com.abid.androidlatihanmix_abid.Preferences
import com.abid.androidlatihanmix_abid.R
import com.abid.androidlatihanmix_abid.model.BukuModel
import com.bumptech.glide.Glide

class BukuAdapter : RecyclerView.Adapter<BukuAdapter.BukuViewHolder> {
    var mContext: Context
    var itemBuku: List<BukuModel>
    var listener: FirebaseDataListener
    lateinit var Pref: Preferences

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BukuViewHolder {
        val view: View = LayoutInflater.from(p0.context).inflate(R.layout.activity_main, p0, false)
        val bukuViewHolder = BukuViewHolder(view)

        return bukuViewHolder
    }

    override fun getItemCount(): Int {
        return itemBuku.size
    }

    override fun onBindViewHolder(p0: BukuViewHolder, p1: Int) {
        val bukuModel: BukuModel = itemBuku.get(p1)
        val pref = Preferences(mContext)
        Glide.with(mContext).load(bukuModel.geturlImage())
            .centerCrop()
            .into(p0.imageRv)
        p0.tv_deskripsi.text = bukuModel.getJudul()
        p0.ll_content.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                val builder = AlertDialog.Builder(mContext)
                builder.setMessage("Pilih Operasi Data")
                builder.setPositiveButton("Update") { dialog, i ->
                    listener.onUpdateData(bukuModel, p1)
                }
                builder.setNegativeButton("Delete") { dialog, i ->
                    listener.onDeleteData(bukuModel, p1)
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
                return true
            }
        })
        p0.rc_view2.setOnClickListener {
            Toast.makeText(mContext, "Contoh touch listener", Toast.LENGTH_SHORT).show()
        }
    }

    constructor(mContext: Context, list: List<BukuModel>) {
        this.mContext = mContext
        this.itemBuku = list
        listener = mContext as MainActivity
    }

    inner class BukuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rc_view2: LinearLayout
        var tv_deskripsi: TextView
        var ll_content: LinearLayout
        var imageRv: ImageView

        init {
            imageRv = itemView.findViewById(R.id.imageStorage)
            rc_view2 = itemView.findViewById(R.id.ll_content)
            tv_deskripsi = itemView.findViewById(R.id.tv_deskripsi)
            ll_content = itemView.findViewById(R.id.ll_content)
        }
    }

    interface FirebaseDataListener {
        fun onDeleteData(buku: BukuModel, position: Int)
        fun onUpdateData(buku: BukuModel, position: Int)
    }
}