package com.automation.petmatch.viewcontrollers.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.automation.petmatch.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.content_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var  mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.database_reference))

        registerButton.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser(){
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = passwordConfirmEditText.text.toString()

        if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()){
            if(password.equals(confirmPassword)){
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this@RegisterActivity){ task ->
                    if(task.isSuccessful){
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        val user = mAuth.currentUser
                        val uid = user!!.uid
                        mDatabase.child("Users").child(uid).child("Name").setValue(name)
                        mDatabase.child("Users").child(uid).child("Email").setValue(email)
                        mDatabase.child("Users").child(uid).child("Password").setValue(password)
                    }
                    else{
                        when(task.exception.toString()) {
                            getString(R.string.weak_password) -> {
                                Toast.makeText(this@RegisterActivity, "Password must contain at least 6 characters", Toast.LENGTH_SHORT).show()
                            }
                            getString(R.string.already_registered) -> {
                                Toast.makeText(this@RegisterActivity, "That email adress is already related with an account", Toast.LENGTH_SHORT).show()
                            }
                            getString(R.string.invalid_email) -> {
                                Toast.makeText(this@RegisterActivity, "Insert a valid email address", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(this@RegisterActivity, "Something went wrong. Please try later", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            else{
                Toast.makeText(this@RegisterActivity, "Please, complete all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
