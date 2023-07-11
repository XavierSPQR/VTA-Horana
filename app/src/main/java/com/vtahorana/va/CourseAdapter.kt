package com.vtahorana.va

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CourseAdapter(private var courseList: List<User>,private val context: Context) : RecyclerView.Adapter<CourseAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.course_item,parent,false)
        return MyViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem=courseList[position]
        holder.CName.text = currentitem.Name
        holder.CDuration.text=currentitem.Duration
        holder.CFee.text= currentitem.Fee
        Glide.with(holder.itemView).load(currentitem.CourseImage).into(holder.CImage)
        val relativeLayout = holder.relativeLayout
        holder.recCard.setOnClickListener {
            val intent = Intent(context, expandCourseDetails::class.java)
            intent.putExtra("Image", courseList[holder.adapterPosition].CourseImage)
            intent.putExtra("Description", courseList[holder.adapterPosition].Description)
            intent.putExtra("Name", courseList[holder.adapterPosition].Name)
            intent.putExtra("Level", courseList[holder.adapterPosition].NvqLevel)
            intent.putExtra("Fee", courseList[holder.adapterPosition].Fee)
            intent.putExtra("Type", courseList[holder.adapterPosition].CourseType)
            intent.putExtra("Duration", courseList[holder.adapterPosition].Duration)
            intent.putExtra("Category", courseList[holder.adapterPosition].CourseCategory)
            context.startActivity(intent)
        }


    }
    fun searchDataList(searchList: List<User>) {
        courseList = searchList
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val CName:TextView=itemView.findViewById(R.id.txtCourseName)
        val CDuration:TextView=itemView.findViewById(R.id.txtDuration)
        val CFee:TextView=itemView.findViewById(R.id.txtFee)
        val CImage:ImageView=itemView.findViewById(R.id.imgviewCourseImage)
        val relativeLayout: RelativeLayout = itemView.findViewById(R.id.crelative)
        val recCard:CardView=itemView.findViewById(R.id.recCard)


    }
}