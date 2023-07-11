package com.vtahorana.va

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.vtahorana.va.databinding.ActivityExpandCourseDetailsBinding

class expandCourseDetails : AppCompatActivity() {
    var imageUrl = ""
    private lateinit var context: Context
    private lateinit var Img:String
    private lateinit var binding:ActivityExpandCourseDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpandCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context=this
        val bundle = intent.extras
        if (bundle != null) {
            Img= bundle.getString("Image").toString()
            binding.txtEDetailsCourseDesc.text = bundle.getString("Description")
            binding.txtEDetailCourseName.text = bundle.getString("Name")
            binding.txtEDetailsCourseCategory.text = bundle.getString("Category")
            binding.txtEDetailsCourseDuration.text = bundle.getString("Duration")
            binding.txtEDetailsCourseFee.text = bundle.getString("Fee")
            binding.txtEDetailsCourseType.text = bundle.getString("Type")
            binding.txtEDetailsNvqLevel.text = bundle.getString("Level")
            imageUrl = bundle.getString("Image")!!
            Glide.with(this).load(bundle.getString("Image"))
                .into(binding.imgviewEDetailImage)
        }
        binding.btnEnroll.setOnClickListener {
            var intent= Intent(context,RegistrationForm::class.java)
            intent.putExtra("course",binding.txtEDetailCourseName.text.toString())
            startActivity(intent)
        }
        binding.btnContact.setOnClickListener {
            // TODO: contact form 
        }
    }
}