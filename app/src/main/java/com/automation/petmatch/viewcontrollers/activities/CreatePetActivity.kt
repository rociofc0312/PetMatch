package com.automation.petmatch.viewcontrollers.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.automation.petmatch.R
import kotlinx.android.synthetic.main.content_create_pet.*

class CreatePetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pet)

        val genreAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_genre, android.R.layout.simple_spinner_item)
        val typeAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_type, android.R.layout.simple_spinner_item)
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genreSpinner.adapter = genreAdapter
        typeSpinner.adapter = typeAdapter
    }
}
