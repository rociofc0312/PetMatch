package com.automation.petmatch.viewcontrollers.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.automation.petmatch.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.content_create_pet.*
import java.io.File
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.OnProgressListener
import android.util.Log
import com.automation.petmatch.model.Pet
import com.google.android.gms.tasks.OnFailureListener
import kotlinx.android.synthetic.main.item_my_pet.*

class CreatePetActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private val STORAGE_PERMISSION_CODE = 123
    private lateinit var filePath: Uri

    private lateinit var petId: String

    private lateinit var mStorage: FirebaseStorage
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDataBase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pet)

        mAuth = FirebaseAuth.getInstance()
        mDataBase = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.database_reference))
        mStorage = FirebaseStorage.getInstance()


        progress_bar.visibility = View.GONE
        val genreAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_genre, R.layout.custom_spinner)
        val typeAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_type, R.layout.custom_spinner)
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genreSpinner.adapter = genreAdapter
        typeSpinner.adapter = typeAdapter

        requestStoragePermission()

        saveButton.setOnClickListener {
            uploadImageToStorage()
        }

        buttonChoose.setOnClickListener {
            showFileChooser()
        }


        val intent = intent?: return
        petId =""
        if (intent.extras!=null) {
            val pet = Pet.from(intent.extras!!)
            val genreSpinnerPosition = genreAdapter.getPosition(pet.Genre)
            val typeSpinnerPosition = typeAdapter.getPosition(pet.Type)

            petId = pet.Name + pet.Owner
            nameEditText.setText(pet.Name)
            genreSpinner.setSelection(genreSpinnerPosition)
            typeSpinner.setSelection(typeSpinnerPosition)
            aboutEditText.setText(pet.About)
            birthDateEditText.setText(pet.Birthdate)
            imageNameEditText.setText(pet.Photo)

            //mDataBase.child("Pets").child(petId)
        }

    }

    private fun createPet(webpath: String) {
        val name = nameEditText.text.toString()
        val about = aboutEditText.text.toString()
        val genre = genreSpinner.selectedItem.toString()
        val type = typeSpinner.selectedItem.toString()
        val birthDate = birthDateEditText.text.toString()

        if (!name.isEmpty() && !about.isEmpty()){
            startActivity(Intent(this@CreatePetActivity, MainActivity::class.java))
            val user = mAuth.currentUser
            val owner = user!!.uid
            if(petId == ""){
                petId = name + owner }
            mDataBase.child("Pets").child(petId).child("Owner").setValue(owner)
            mDataBase.child("Pets").child(petId).child("Name").setValue(name)
            mDataBase.child("Pets").child(petId).child("About").setValue(about)
            mDataBase.child("Pets").child(petId).child("Genre").setValue(genre)
            mDataBase.child("Pets").child(petId).child("Type").setValue(type)
            mDataBase.child("Pets").child(petId).child("Birthdate").setValue(birthDate)
            mDataBase.child("Pets").child(petId).child("Photo").setValue(webpath)

        }
    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    public override fun onActivityResult(requestCode: Int, result: Int, data: Intent?) {
        super.onActivityResult(requestCode, result, data)
        filePath = data!!.data!!
        imageNameEditText.setText(File(getPath(filePath)).name)
    }

    private fun getPath(uri: Uri): String {
        var cursor = contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        var document_id = cursor.getString(0)
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
        cursor.close()
        cursor = contentResolver.query(
            android
                .provider
                .MediaStore
                .Images
                .Media
                .EXTERNAL_CONTENT_URI, null, MediaStore
                .Images
                .Media
                ._ID + " = ? ", arrayOf(document_id), null)
        cursor!!.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor.close()
        return path
    }

    private fun requestStoragePermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show()
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun uploadImageToStorage(){
        var file = Uri.fromFile(File(getPath(filePath)))
        val riversRef = mStorage.reference.child("images/${file.lastPathSegment}")

        progress_bar.visibility = View.VISIBLE
        buttonChoose.isEnabled = false
        saveButton.isEnabled = false
        var uploadTask = riversRef.putFile(file)

        uploadTask.addOnSuccessListener{
            progress_bar.visibility = View.GONE
            riversRef.downloadUrl.addOnSuccessListener {
                Log.d("Create pet", "Location: $it")
                createPet("$it")
                Toast.makeText(this@CreatePetActivity, "Uploaded image and saved pet.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener(OnFailureListener { e ->
            progress_bar.visibility = View.GONE
            buttonChoose.isEnabled = true
            saveButton.isEnabled = true
            Toast.makeText(this@CreatePetActivity, "Failed " + e.message, Toast.LENGTH_SHORT).show()
        }).addOnProgressListener(OnProgressListener<UploadTask.TaskSnapshot> {
        })
    }
}
