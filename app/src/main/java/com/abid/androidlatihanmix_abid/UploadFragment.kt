package com.abid.androidlatihanmix_abid

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_upload.*
import java.io.IOException
import java.util.*


class UploadFragment : Fragment() {

    val PERMISSION_REQUEST_CODE = 10003
    lateinit var filePathImage: Uri
    lateinit var helperPref: Preferences
    val REQUEST_IMAGE = 10002

    var value = 0.0
    var datax: String? = null
    var counter = 0

    lateinit var dbRef: DatabaseReference
    lateinit var fStorage: FirebaseStorage
    lateinit var fStorageRef: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helperPref = Preferences(context!!)
        counter = helperPref.getCounterId()
        fStorage = FirebaseStorage.getInstance()
        fStorageRef = fStorage.reference
        datax = activity!!.intent.getStringExtra("kode")
        btn_kirim.setOnClickListener {
            val desc = et_keterangan.text.toString()
            helperPref = Preferences(context!!)
            if (desc.isNotEmpty()) {
                simpanToFirebase(desc)
                if (datax == null) {
                    helperPref.saveCounterId(counter + 1)
                }
            } else {
                Toast.makeText(context, "Inputan harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
        // Inflate the layout for this fragment
        img_placeholder.setOnClickListener {
            when {
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) -> {
                    if (ContextCompat.checkSelfPermission(
                            context!!,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermissions(
                            arrayOf(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            PERMISSION_REQUEST_CODE
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
                    Toast.makeText(context, "Izin ditolak!", Toast.LENGTH_SHORT).show()
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
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, filePathImage)
                    Glide.with(this).load(bitmap).override(250, 250).centerCrop().into(img_placeholder)
                } catch (x: IOException) {
                    x.printStackTrace()
                }
            }
        }
    }

    fun GetFileExtension(uri: Uri): String? {
        val contentResolverz = context!!.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolverz.getType(uri))
    }

    fun simpanToFirebase(desc: String) {
        val uidUser = helperPref.getUID()
        val nameX = UUID.randomUUID().toString()
        val ref: StorageReference = fStorageRef.child("images/$uidUser/${nameX}.${GetFileExtension(filePathImage)}")
        ref.putFile(filePathImage).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                dbRef = FirebaseDatabase.getInstance().getReference("dataBuku/$uidUser/$counter")
                dbRef.child("/urlImage").setValue(it.toString())
                dbRef.child("/id").setValue(uidUser)
                dbRef.child("/desc").setValue(desc)
            }
            Toast.makeText(
                context,
                "Success Upload",
                Toast.LENGTH_SHORT
            ).show()
            progressDownload.visibility = View.GONE
        }.addOnFailureListener {
            Log.e("TAGERROR", it.message)
            progressDownload.visibility = View.GONE
        }
            .addOnProgressListener { taskSnapshot ->
                value = (100.0 * taskSnapshot
                    .bytesTransferred / taskSnapshot.totalByteCount)
                progressDownload.visibility = View.VISIBLE
            }
        Toast.makeText(context, "Data berhasil tambah!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(context, MainActivity::class.java))
    }
    // TODO: Rename method, update argument and hook method into UI event
}
