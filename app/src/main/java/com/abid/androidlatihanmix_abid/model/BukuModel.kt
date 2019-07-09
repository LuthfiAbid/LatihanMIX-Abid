package com.abid.androidlatihanmix_abid.model

class BukuModel {
    private var nama: String? = null
    private var tanggal: String? = null
    private var judul: String? = null
    private var id: String? = null
    private var desc: String? = null
    private var key: String? = null
    private var namaImage: String? = null
    private var urlImage: String? = null

    constructor()
    constructor(nama: String, tanggal: String, judul: String, desc: String) {
        this.nama = nama
        this.tanggal = tanggal
        this.judul = judul
        this.desc = desc
        this.namaImage = namaImage
        this.urlImage = urlImage
    }

    //GET
    fun getNama(): String {
        return nama!!
    }

    fun getTanggal(): String {
        return tanggal!!
    }

    fun getJudul(): String {
        return judul!!
    }

    fun getId(): String {
        return id!!
    }

    fun getDesc(): String {
        return desc!!
    }

    fun getKey(): String {
        return key!!
    }

    fun getnamaImage(): String {
        return namaImage!!
    }

    fun geturlImage(): String {
        return urlImage!!
    }

    //SET
    fun setNama(nama: String) {
        this.nama = nama
    }

    fun setTangga(tanggal: String) {
        this.tanggal = tanggal
    }

    fun setJudul(judul: String) {
        this.judul = judul
    }

    fun setId(id: String) {
        this.id = id
    }

    fun setDesc(desc: String) {
        this.desc = desc
    }

    fun setKey(key: String) {
        this.key = key
    }
}