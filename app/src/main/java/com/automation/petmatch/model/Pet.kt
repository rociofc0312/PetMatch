package com.automation.petmatch.model

import android.os.Bundle


data class Pet(
    val Name: String,
    val Genre: String,
    val Type: String,
    val About: String,
    val Birthdate: String,
    val Owner: String,
    val Photo: String
) {
    constructor(): this("", "","", "", "","", "")
    companion object {
        fun from(bundle: Bundle) : Pet{
            return Pet(
                    bundle.getString("Name")!!,
                    bundle.getString("Genre")!!,
                    bundle.getString("Type")!!,
                    bundle.getString("About")!!,
                    bundle.getString("Birthdate")!!,
                    bundle.getString("Owner")!!,
                    bundle.getString("Photo")!!
            )
        }
    }

    fun toBundle(): Bundle{
        val bundle = Bundle()
        with(bundle){
            putString("Name", Name)
            putString("Genre", Genre)
            putString("Type", Type)
            putString("About", About)
            putString("Birthdate", Birthdate)
            putString("Owner", Owner)
            putString("Photo", Photo)
        }
        return bundle
    }
}
