package com.vtahorana.va

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vtahorana.va.databinding.ActivityCoursesBinding
import java.util.Locale


class Courses : AppCompatActivity() {
    var eventListener: ValueEventListener? = null
    private lateinit var dbref: DatabaseReference
    private lateinit var courseArrayList: ArrayList<User>
    private lateinit var adapter: CourseAdapter
    private lateinit var binding: ActivityCoursesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val gridLayoutManager = GridLayoutManager(this@Courses, 1)
        binding.courselist.layoutManager = gridLayoutManager
        binding.search.clearFocus()
        val builder = AlertDialog.Builder(this@Courses)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        courseArrayList = ArrayList()
        adapter = CourseAdapter( courseArrayList,this@Courses)
        binding.courselist.adapter = adapter
        dbref = FirebaseDatabase.getInstance().getReference("Courses")
        dialog.show()

        eventListener = dbref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                courseArrayList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(User::class.java)
                    if (dataClass != null) {
                        courseArrayList.add(dataClass)
                    }
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newT: String?): Boolean {
                if (newT != null) {
                    searchList(newT)
                }
                return true
            }

        })

    }
    fun searchList(text: String) {
        val searchList = java.util.ArrayList<User>()
        for (dataClass in courseArrayList) {
            if (dataClass.Name?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true
            ) {
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)
    }
}