package com.automation.petmatch.viewcontrollers.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.automation.petmatch.R
import com.automation.petmatch.viewcontrollers.fragments.PetsFragment
import com.automation.petmatch.viewcontrollers.fragments.UserFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        return@OnNavigationItemSelectedListener navigateTo(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
                view -> startActivity(Intent(view.context, CreatePetActivity::class.java))
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_pets
    }

    private fun fragmentFor(item: MenuItem): Fragment {
        when (item.itemId) {
            R.id.navigation_pets -> {
                return PetsFragment()
            }
            R.id.navigation_favorites -> {
                return PetsFragment()
            }
            R.id.navigation_user -> {
                return UserFragment()
            }
        }
        return PetsFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                if (supportFragmentManager.backStackEntryCount >= 1){
                    supportFragmentManager.popBackStack()
                }
            }
        }
        return true
    }

    private fun navigateTo(item: MenuItem): Boolean {
        item.setChecked(true)
        return supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContent, fragmentFor(item))
            .commit() > 0
    }
}
