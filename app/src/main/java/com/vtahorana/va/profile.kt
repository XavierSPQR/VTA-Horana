package com.vtahorana.va

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vtahorana.va.databinding.ActivityCoursesBinding
import com.vtahorana.va.databinding.ActivityProfileBinding

class profile : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val myVariable:String = sharedPreferences.getString("myVariableKey", null).toString()
        databaseReference=FirebaseDatabase.getInstance().getReference("Students")
        databaseReference.child(myVariable).get().addOnSuccessListener {
            if(it.exists())
            {
                val sname=it.child("name").value
                val sfname=it.child("fullname").value
                val sfadd=it.child("address").value
                val sfbir=it.child("birthday").value
                val sfcn=it.child("course").value
                val sfemail=it.child("email").value
                val sfgen=it.child("gender").value
                val simage=it.child("image").value
                val snic=it.child("nic").value
                val sphone=it.child("phone").value
                Toast.makeText(this,"your data successfully submitted",Toast.LENGTH_SHORT).show()
                binding.txtproStudentName.text = sname.toString()
                binding.txtproStudentFullName.text=sfname.toString()
                binding.txtproGender.text=sfgen.toString()
                binding.txtproDob.text=sfbir.toString()
                binding.txtproStudentAddress.text=sfadd.toString()
                binding.txtproStudentCourseSelected.text=sfcn.toString()
                binding.txtproStudentEmail.text=sfemail.toString()
                binding.txtproStudentNic.text=snic.toString()
                binding.txtproStudentPhone.text=sphone.toString()
                Glide.with(this).load(simage.toString())
                    .into(binding.profileStudentImage)
            }else{
                Toast.makeText(this,"not Found",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
        }
        fun onBackPressed() {
           var intent=Intent(this,HomeScreen::class.java)
            startActivity(intent)

            // Call super.onBackPressed() to allow the default back button behavior
            super.onBackPressed()
        }
    }
}