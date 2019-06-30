package com.abid.androidlatihanmix_abid

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {
    private val PREFS_NAME = "TEST"
    val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setStatusInput(status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean("STATUS", status)
        editor.apply()
    }

    fun cekStatus(): Boolean {
        return sharedPref.getBoolean("STATUS", false)
    }

    fun setEmail(email: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("EMAIL", email)
        editor.apply()
    }

    fun getEmail(): String {
        return sharedPref.getString("EMAIL", "")
    }
}
