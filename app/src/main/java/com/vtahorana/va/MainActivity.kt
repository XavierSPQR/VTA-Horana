package com.vtahorana.va

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.vtahorana.va.adapter.OnboardingViewPageAdapter
import com.vtahorana.va.model.OnboardingData

class MainActivity : AppCompatActivity() {
    var onboardingViewPageAdapter:OnboardingViewPageAdapter?=null
    var tabLayout:TabLayout?=null
    var onBoardingViewPager: ViewPager?=null
    var next:TextView?=null
    var position=0
    var sharedPreferences : SharedPreferences?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        val relativeLayout = findViewById<RelativeLayout>(R.id.relativel)
        val animationDrawable = relativeLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(5000)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()



        tabLayout=findViewById(R.id.tab_indicator)
        next=findViewById(R.id.btnNext)

    //    if(restorePrefData())
    //    {
     //      val i= Intent(applicationContext,HomeScreen::class.java)
     //       startActivity(i)
     //       finish()
     //  }


        val onboardingData:MutableList<OnboardingData> = ArrayList()
        onboardingData.add(OnboardingData("Welcome","desc 1",R.drawable.ic_launcher_foreground))
        onboardingData.add(OnboardingData("Browse Courses","desc 02",R.drawable.ic_launcher_foreground))
        onboardingData.add(OnboardingData("Get Suggestions","desc 03",R.drawable.ic_launcher_foreground))
        onboardingData.add(OnboardingData("Register","desc 04",R.drawable.ic_launcher_foreground))
        onboardingData.add(OnboardingData("Chat","desc 05",R.drawable.ic_launcher_foreground))
        onboardingData.add(OnboardingData("View Events","desc 06",R.drawable.ic_launcher_foreground))

        setOnBoardingViewAdapter(onboardingData)
        position = onBoardingViewPager!!.currentItem
        next?.setOnClickListener {
            if(position<onboardingData.size)
            {
                position++
                onBoardingViewPager!!.currentItem=position
            }
            if(position==onboardingData.size)
            {
      //          savePrefData()
                val i= Intent(applicationContext,HomeScreen::class.java)
                startActivity(i)

           }
        }
        tabLayout!!.addOnTabSelectedListener(object :OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                position=tab!!.position
                if(tab?.position==onboardingData.size -1){
                    next!!.text="Get Started"
                }
                else
                {
                    next!!.text="Next"
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })


    }
    private fun setOnBoardingViewAdapter(onboardingData: List<OnboardingData>){

        onBoardingViewPager = findViewById(R.id.screenPager);
        onboardingViewPageAdapter = OnboardingViewPageAdapter(this,onboardingData)
        onBoardingViewPager!!.adapter=onboardingViewPageAdapter
        tabLayout?.setupWithViewPager(onBoardingViewPager)

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