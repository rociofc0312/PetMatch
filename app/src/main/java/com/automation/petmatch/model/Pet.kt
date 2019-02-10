package com.automation.petmatch.model

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
}
