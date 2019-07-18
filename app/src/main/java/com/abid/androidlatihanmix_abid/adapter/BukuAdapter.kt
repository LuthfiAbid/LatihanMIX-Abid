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
        val view: View = LayoutInflater.from(p0.context).inflate(R.layout.activity_show_data, p0, false)
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
        p0.tv_nama.text = bukuModel.getNama()
        p0.tv_tanggal.text = bukuModel.getTanggal()
        p0.tv_judul.text = bukuModel.getJudul()
        p0.tv_desc.text = bukuModel.getDesc()
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
        var tv_nama: TextView
        var tv_tanggal: TextView
        var tv_judul: TextView
        var tv_desc: TextView
        var ll_content: LinearLayout
        var imageRv: ImageView

        init {
            imageRv = itemView.findViewById(R.id.imageStorage)
            rc_view2 = itemView.findViewById(R.id.ll_content)
            tv_nama = itemView.findViewById(R.id.tv_nama)
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal)
            tv_judul = itemView.findViewById(R.id.tv_title)
            tv_desc = itemView.findViewById(R.id.tv_desc)
            ll_content = itemView.findViewById(R.id.ll_content)
        }
    }

    interface FirebaseDataListener {
        fun onDeleteData(buku: BukuModel, position: Int)
        fun onUpdateData(buku: BukuModel, position: Int)
    }
}