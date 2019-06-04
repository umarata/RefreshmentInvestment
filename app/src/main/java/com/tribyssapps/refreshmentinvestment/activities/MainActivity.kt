package com.tribyssapps.refreshmentinvestment.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import android.os.Bundle

import com.tribyssapps.refreshmentinvestment.R
import com.tribyssapps.refreshmentinvestment.fragments.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addNewFragment(MainFragment())
    }

    fun addNewFragment(fragment: Fragment) {
        if (fragment is MainFragment) {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commitAllowingStateLoss()
        } else {

            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack("report").commitAllowingStateLoss()
        }

    }


}
