package com.automation.petmatch.viewcontrollers.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import com.automation.petmatch.R
import com.automation.petmatch.model.Pet
import com.automation.petmatch.viewcontrollers.adapters.PetsAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_pets.view.*

class PetsFragment : Fragment() {

    lateinit var mDatabase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference

    private lateinit var petsRecyclerView: RecyclerView
    private lateinit var petsAdapter: PetsAdapter
    private lateinit var petsLayoutManager: RecyclerView.LayoutManager

    private lateinit var pets: MutableList<Pet>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pets, container, false)

        pets = mutableListOf()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.getReference("Pets")

        petsRecyclerView = view.petsRecyclerView
        petsAdapter = PetsAdapter(false,pets, view.context)
        petsLayoutManager = GridLayoutManager(view.context, 1, GridLayoutManager.HORIZONTAL, false) as RecyclerView.LayoutManager
            //LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)

        petsRecyclerView.adapter = petsAdapter
        petsRecyclerView.layoutManager = petsLayoutManager

        mDatabaseReference.addValueEventListener(object: ValueEventListener{
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
