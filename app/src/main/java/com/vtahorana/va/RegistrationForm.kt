package com.vtahorana.va

import android.R
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.vtahorana.va.databinding.ActivityRegistrationFormBinding
import java.util.Calendar
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class RegistrationForm : AppCompatActivity() {

    private lateinit var genderselected:String
    private lateinit var subject:String
    private lateinit var selectedcourse:String
    private lateinit var message:String
    private lateinit var recipientEmail:String
    private lateinit var senderEmail:String
    var sharedPreferences : SharedPreferences?=null

    private lateinit var senderPassword:String
    private lateinit var sendEmailTask: SendEmailTask
    private lateinit var context:Context

    private var imageURL: String? = null
    private var uri: Uri? = null
    var imageUrl = ""
    private lateinit var formattedDate:String
    private lateinit var binding:ActivityRegistrationFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context=this


        if(restorePrefData())
            {
             val i= Intent(applicationContext,HomeScreen::class.java)
               startActivity(i)
               finish()
          }

        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val bundle = intent.extras
        if (bundle != null) {
           binding.txtStudentCourseSelected.setText(bundle.getString("course").toString())
        }
        senderEmail = "prabhaththarangaspqr@gmail.com"
        senderPassword = "ozkhyrspwshsveoc"
        binding.btnSubmitdata.isEnabled = false
        binding.dateEditText.setOnClickListener{
            showDatePicker()
        }
        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                uri = data!!.data
                binding.uploadStudentImage.setImageURI(uri)
            } else {
                Toast.makeText(this@RegistrationForm, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }
        val genderoptions = listOf("Select Gender","Male","Female")
        val genderadapter = ArrayAdapter(this, R.layout.simple_spinner_item,genderoptions)
        genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnStudentGender.adapter = genderadapter
        binding.spnStudentGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                genderselected= parent.getItemAtPosition(position) as String
                if(position !== 0)
                {
                    binding.btnSubmitdata.isEnabled=true
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        binding.uploadStudentImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }
        binding.btnSubmitdata.setOnClickListener {
           //
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm detail submission")
            builder.setMessage("Please review if your details are correct. This action can not be undone.")
            builder.setPositiveButton("Confirm") { dialog, which ->
                saveData()

                val editor = sharedPreferences.edit()
                val myVariable =binding.edtStudentName.text.toString()
                editor.putString("myVariableKey", myVariable)
                editor.apply()

                recipientEmail = binding.edtStudentEmail.text.toString()
                 subject = "Welcome :"+binding.edtStudentName.text.toString()
                message = "This is the email body."
            }
            builder.setNegativeButton("Review") { dialog, which ->

            }

            val dialog = builder.create()
            dialog.show()
        }

    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                formattedDate = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
                binding.dateEditText.setText(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()


    }

    private fun saveData() {
          try {
            val storageReference = FirebaseStorage.getInstance().reference.child("StudentImages")
                .child(uri!!.lastPathSegment!!)
            val builder = AlertDialog.Builder(this@RegistrationForm)
            builder.setCancelable(false)
            builder.setView(com.vtahorana.va.R.layout.progress_layout)
            val dialog = builder.create()
            dialog.show()
            storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isComplete);
                val urlImage = uriTask.result
                imageURL = urlImage.toString()
                uploadData()

            }.addOnFailureListener {
                dialog.dismiss()
            }
        } catch (nullpoint: NullPointerException ) {
              Toast.makeText(this@RegistrationForm, "No Image Selected", Toast.LENGTH_SHORT).show()
              var intent=Intent(this,RegistrationForm::class.java)
              startActivity(intent)

        }
    }






    private fun uploadData() {
        var sname=binding.edtStudentName.text.toString()
        var fullname=binding.edtStudentFullName.text.toString()
        var nic=binding.edtStudentNic.text.toString()
        var address=binding.edtStudentAddress.text.toString()
        var phone=binding.edtStudentPhone.text.toString()
        var course=binding.txtStudentCourseSelected.text.toString()
        var email=binding.edtStudentEmail.text.toString()



        val dataClass=StudentRegistrationDataClass(
            imageURL,
            sname,
            fullname,
            genderselected,
            nic,
            formattedDate,
            address,
            phone,
            course,
            email)
        FirebaseDatabase.getInstance().getReference("Students").child(sname)
            .setValue(dataClass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@RegistrationForm, "Saved", Toast.LENGTH_SHORT).show()
                    sendEmailTask = SendEmailTask(senderEmail, senderPassword, recipientEmail, subject, message)
                    sendEmailTask.execute()
                    Toast.makeText(this@RegistrationForm, "Check your Email", Toast.LENGTH_SHORT).show()
                  //  savePrefData()
                    finish()
                    var intent=Intent(this,profile::class.java)
                    startActivity(intent)

                }
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this@RegistrationForm, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun savePrefData()
    {
        sharedPreferences=applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.putBoolean("IsFirstTimeRun",true)
        editor.apply()

    }
    private fun restorePrefData(): Boolean
    {
        sharedPreferences=applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("IsFirstTimeRun",false)
    }
    }

