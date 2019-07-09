package com.abid.androidlatihanmix_abid.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.abid.androidlatihanmix_abid.HomeFragment
import com.abid.androidlatihanmix_abid.UploadFragment

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) { // sebuah list yang menampung objek Fragment
    private val pages = listOf(
        HomeFragment(),
        UploadFragment()
    )

    // menentukan fragment yang akan dibuka pada posisi tertentu
    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    // judul untuk tabs
    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "First Tab"
            else -> "Second Tab"
        }
    }
}