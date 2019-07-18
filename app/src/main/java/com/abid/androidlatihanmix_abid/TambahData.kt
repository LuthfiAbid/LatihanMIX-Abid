package com.abid.androidlatihanmix_abid

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.abid.androidlatihanmix_abid.model.BukuModel
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_upload.*
import java.io.IOException
import java.util.*

class TambahData : AppCompatActivity() {

    val REQUEST_IMAGE = 10002
    val PERMISSION_REQUEST_CODE = 10003
    lateinit var filePathImage: Uri

    lateinit var dbRef: DatabaseReference
    lateinit var helperPref: Preferences
    private var list: MutableList<BukuModel>? = null
    lateinit var fStorage: FirebaseStorage
    lateinit var fStorageRef: StorageReference
    var datax: String? = null
    var counter = 0
    var value = 0.0
    val REQUEST_CODE_IMAGE = 10002
    val PERMISSION_RC = 10003


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_data)
        datax = intent.getStringExtra("kode")
        helperPref = Preferences(this)
        counter = helperPref.getCounterId()
        fStorage = FirebaseStorage.getInstance()
        fStorageRef = fStorage.reference
        if (datax != null) {
            showDataFromDB()
            counter = datax!!.toInt()
        }

        img_placeholder.setOnClickListener {
            when {
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) -> {
                    if (ContextCompat.checkSelfPermission(
                            this@TambahData,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ), PERMISSION_RC
                        )
                    } else {
                        imageChooser()
                    }
                }
                else -> {
                    imageChooser()
                }
            }
        }

        btn_submit.setOnClickListener {
            val nama = et_nama.text.toString()
            val tgl = et_tanggal.text.toString()
            val judul = et_judul.text.toString()
            val desc = et_desc.text.toString()
            helperPref = Preferences(this)
            if (nama.isNotEmpty() || judul.isNotEmpty() || desc.isNotEmpty() || tgl.isNotEmpty()) {
                simpanToFirebase(nama, judul, tgl, desc)
                if (datax == null) {
                    helperPref.saveCounterId(counter + 1)
                }
            } else {
                Toast.makeText(this, "Inputan harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun GetFileExtension(uri: Uri): String? {
        val contentResolverz = this.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolverz.getType(uri))
    }

    private fun imageChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select Image"),
            REQUEST_IMAGE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Izin ditolak!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_IMAGE -> {
                filePathImage = data?.data!!
                try {
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, filePathImage)
                    Glide.with(this).load(bitmap).override(250, 250).centerCrop().into(img_placeholder)
                } catch (x: IOException) {
                    x.printStackTrace()
                }
            }
        }
    }

    fun simpanToFirebase(nama: String, judul: String, tgl: String, desc: String) {
        val uidUser = helperPref.getUID()
        val nameX = UUID.randomUUID().toString()
        val ref: StorageReference = fStorageRef.child("images/$uidUser/${nameX}.${GetFileExtension(filePathImage)}")
        ref.putFile(filePathImage).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                dbRef = FirebaseDatabase.getInstance().getReference("dataBuku/$uidUser/$counter")
                dbRef.child("/urlImage").setValue(it.toString())
                dbRef.child("/id").setValue(uidUser)
                dbRef.child("/nama").setValue(nama)
                dbRef.child("/judul").setValue(judul)
                dbRef.child("/tanggal").setValue(tgl)
                dbRef.child("/desc").setValue(desc)
            }
            Toast.makeText(
                this@TambahData,
                "Success Upload",
                Toast.LENGTH_SHORT
            ).show()
            progressDownloadd.visibility = View.GONE
        }.addOnFailureListener {
            Log.e("TAGERROR", it.message)
            progressDownloadd.visibility = View.GONE
        }
            .addOnProgressListener { taskSnapshot ->
                value = (100.0 * taskSnapshot
                    .bytesTransferred / taskSnapshot.totalByteCount)
                progressDownloadd.visibility = View.VISIBLE
            }
        Toast.makeText(this, "Data berhasil tambah!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, HomeFragment::class.java))
    }

    fun showDataFromDB() {
        dbRef = FirebaseDatabase.getInstance().getReference("dataBuku/${helperPref.getUID()}")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    et_nama.setText(data.child("nama").value.toString())
                    et_judul.setText(data.child("judul").value.toString())
                    et_tanggal.setText(data.child("tanggal").value.toString())
                    et_desc.setText(data.child("desc").value.toString())
                }
            }
        })
    }
}