package com.vtahorana.va

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.vtahorana.va.databinding.ActivityHomeScreenBinding

class HomeScreen : AppCompatActivity() {
    private val fromBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_fab)
    }
    private val toBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_fab)
    }
    private val rotateClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_clock_wise)
    }
    private val rotateAntiClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_anti_clock_wise)
    }
    private val fromBottomBgAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim)
    }
    private val toBottomBgAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim)
    }
    private lateinit var binding:ActivityHomeScreenBinding
    private lateinit var context:Context
    private var isExpanded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context=this
        binding=ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainFabBtn.setOnClickListener {

            if (isExpanded) {
                shrinkFab()
            } else {
                expandFab()
            }

        }

        binding.showcourses.setOnClickListener {
           var intent=Intent(context,Courses::class.java)
            startActivity(intent)
        }
        binding.showprofile.setOnClickListener {
            var intent=Intent(context,profile::class.java)
            startActivity(intent)
        }
        binding.btnIns.setOnClickListener {
            var intent=Intent(context,Instructor::class.java)
            startActivity(intent)
        }
        binding.locationFabBtn.setOnClickListener {
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }

        binding.txtLocation.setOnClickListener {
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }
        binding.contactFabBtn.setOnClickListener {
          //  val intent = Intent(this,Instructor::class.java)
           // startActivity(intent)
        }
        binding.txtContact.setOnClickListener {


        }

        binding.chatFabButton.setOnClickListener {
           // val intent = Intent(this,Instructor::class.java)
          //  startActivity(intent)
        }
        binding.txtChat.setOnClickListener {


        }

    }override fun onBackPressed() {

        if (isExpanded) {
            shrinkFab()
        } else {
            super.onBackPressed()

        }
    }
    private fun expandFab() {

        binding.transparentBg.startAnimation(fromBottomBgAnim)
        binding.mainFabBtn.startAnimation(rotateClockWiseFabAnim)
        binding.locationFabBtn.startAnimation(fromBottomFabAnim)
        binding.contactFabBtn.startAnimation(fromBottomFabAnim)
        binding.txtContact.startAnimation(fromBottomFabAnim)
        binding.txtLocation.startAnimation(fromBottomFabAnim)
        binding.txtChat.startAnimation(fromBottomFabAnim)
        binding.chatFabButton.startAnimation(fromBottomFabAnim)

        isExpanded = !isExpanded
    }
    private fun shrinkFab() {
        binding.transparentBg.startAnimation(toBottomBgAnim)
        binding.mainFabBtn.startAnimation(rotateAntiClockWiseFabAnim)
        binding.contactFabBtn.startAnimation(toBottomFabAnim)
        binding.txtContact.startAnimation(toBottomFabAnim)
        binding.locationFabBtn.startAnimation(toBottomFabAnim)
        binding.txtLocation.startAnimation(toBottomFabAnim)
        binding.chatFabButton.startAnimation(toBottomFabAnim)
        binding.txtChat.startAnimation(toBottomFabAnim)

        isExpanded = !isExpanded
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        if (ev?.action == MotionEvent.ACTION_DOWN) {

            if (isExpanded) {
                val outRect = Rect()
                binding.fabConstraint.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    shrinkFab()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}