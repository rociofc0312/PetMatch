package com.automation.petmatch.viewcontrollers.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.automation.petmatch.R
import com.automation.petmatch.model.Pet
import com.automation.petmatch.viewcontrollers.adapters.PetsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.content_create_pet.*
import kotlinx.android.synthetic.main.fragment_pets.view.*
import kotlinx.android.synthetic.main.item_pet.view.*

class UserFragment : Fragment() {

    lateinit var mDatabase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference

    private lateinit var petsOwnerRecyclerView: RecyclerView
    private lateinit var petsAdapter: PetsAdapter
    private lateinit var petsLayoutManager: RecyclerView.LayoutManager
    private lateinit var mAuth: FirebaseAuth

    private lateinit var pets: MutableList<Pet>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pets, container, false)

        pets = mutableListOf()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.getReference("Pets")
        mAuth = FirebaseAuth.getInstance()

        val user = mAuth.currentUser
        val owner = user!!.uid
        val petOwner = mDatabaseReference.orderByChild("Owner").equalTo(owner)

        petsOwnerRecyclerView = view.petsRecyclerView
        petsAdapter = PetsAdapter(true,pets, view.context)

        petsLayoutManager = GridLayoutManager(view.context, 1, GridLayoutManager.VERTICAL, false) as RecyclerView.LayoutManager
        petsOwnerRecyclerView.adapter = petsAdapter
        petsOwnerRecyclerView.layoutManager = petsLayoutManager

        petOwner.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for (h in p0.children){
                            val pet = h.getValue(Pet::class.java)
                            pets.add(pet!!)
                    }
                    petsAdapter.pets = pets
                    petsAdapter.notifyDataSetChanged()
                }
            }
        })

        return view
    }
}
