package com.automation.petmatch.viewcontrollers.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.automation.petmatch.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.content_create_pet.*

class CreatePetActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDataBase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pet)

        mAuth = FirebaseAuth.getInstance()
        mDataBase = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.database_reference))

        val genreAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_genre, android.R.layout.simple_spinner_item)
        val typeAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_type, android.R.layout.simple_spinner_item)
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genreSpinner.adapter = genreAdapter
        typeSpinner.adapter = typeAdapter

        saveButton.setOnClickListener {
            createPet()
        }


    }

    private fun createPet() {
        val name = nameEditText.text.toString()
        val about = aboutEditText.text.toString()
        val genre = genreSpinner.selectedItem.toString()
        val type = typeSpinner.selectedItem.toString()
        val birthDate = birthDateEditText.text.toString()

        if (!name.isEmpty() && !about.isEmpty()){
            startActivity(Intent(this@CreatePetActivity, MainActivity::class.java))
            val user = mAuth.currentUser
            val owner = user!!.uid
            val petId = name + owner
            mDataBase.child("Pets").child(petId).child("Owner").setValue(owner)
            mDataBase.child("Pets").child(petId).child("Name").setValue(name)
            mDataBase.child("Pets").child(petId).child("About").setValue(about)
            mDataBase.child("Pets").child(petId).child("Genre").setValue(genre)
            mDataBase.child("Pets").child(petId).child("Type").setValue(type)
            mDataBase.child("Pets").child(petId).child("Birthdate").setValue(birthDate)
            mDataBase.child("Pets").child(petId).child("Photo").setValue("provisional information.")

        }
    }
}
