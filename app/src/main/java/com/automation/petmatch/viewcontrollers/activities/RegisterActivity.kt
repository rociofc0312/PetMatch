package com.automation.petmatch.viewcontrollers.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.automation.petmatch.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
    }
}
