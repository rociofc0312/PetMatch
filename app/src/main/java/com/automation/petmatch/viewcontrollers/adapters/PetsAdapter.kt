package com.automation.petmatch.viewcontrollers.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.automation.petmatch.R
import com.automation.petmatch.model.Pet
import com.automation.petmatch.viewcontrollers.activities.CreatePetActivity
import kotlinx.android.synthetic.main.item_pet.view.*


class PetsAdapter(var data: Boolean, var pets: List<Pet>, val context: Context): RecyclerView.Adapter<PetsAdapter.ViewHolder>(){

    private var itemInflate = R.layout.item_pet

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        /*if(data){
            itemInflate = R.layout.item_my_pet
        }*/
        val inflator = LayoutInflater.from(context).inflate(itemInflate, p0, false)

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
        val deleteImageView = view.deleteImageView

        fun updateFrom(pet: Pet){
            petImageView.setDefaultImageResId(R.mipmap.ic_launcher)
            petImageView.setErrorImageResId(R.mipmap.ic_launcher)
            petImageView.setImageUrl(pet.Photo)

            nameTextView.text = pet.Name
            birthdateTextView.text = pet.Birthdate
            if(!data){
                deleteImageView.visibility = View.INVISIBLE
            }

            genderImageView.setImageResource(
                if(pet.Genre.equals("Male")){
                    R.drawable.ic_male
                } else {
                    R.drawable.ic_female
                }
            )

            if (data) {
                petLayout.setOnClickListener { view ->
                    val context = view.context

                    context.startActivity(
                        Intent(context, CreatePetActivity::class.java)
                            .putExtras(pet.toBundle())
                    )
                }
                deleteImageView.setOnClickListener {view ->
                    Toast.makeText(view.context, "Wrong username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
