package com.automation.petmatch.viewcontrollers.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.automation.petmatch.R
import com.automation.petmatch.model.Pet
import kotlinx.android.synthetic.main.item_pet.view.*




class PetsAdapter(var pets: List<Pet>, val context: Context): RecyclerView.Adapter<PetsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflator = LayoutInflater.from(context).inflate(R.layout.item_pet, p0, false)

        /*val layoutParams = inflator.getLayoutParams()
        layoutParams.height = p0.height
        inflator.setLayoutParams(layoutParams)*/
        return ViewHolder(inflator)
    }

    override fun getItemCount(): Int {
        return pets.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val pet = pets.get(p1)
        p0.updateFrom(pet)
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val petLayout = view.petLayout
        val petImageView = view.petImageView
        val nameTextView = view.nameTextView
        val genderImageView = view.genderImageView
        val birthdateTextView = view.birthTextview

        fun updateFrom(pet: Pet){
            petImageView.setDefaultImageResId(R.mipmap.ic_launcher)
            petImageView.setErrorImageResId(R.mipmap.ic_launcher)
            petImageView.setImageUrl(pet.Photo)

            nameTextView.text = pet.Name
            birthdateTextView.text = pet.Birthdate

            genderImageView.setImageResource(
                if(pet.Genre.equals("Male")){
                    R.drawable.ic_male
                } else {
                    R.drawable.ic_female
                }
            )

            petLayout.setOnClickListener{

            }
        }
    }
}
