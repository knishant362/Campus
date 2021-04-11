package com.trendster.campus.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.trendster.campus.R
import com.trendster.campus.databinding.ScheduleRowLayoutBinding

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.MyViewHolder>() {

    private var schedule = mutableListOf<DocumentSnapshot?>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = parent.context.getSystemService(LayoutInflater::class.java)
            .inflate(R.layout.schedule_row_layout, parent, false)

        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = schedule[position]?.data
        ScheduleRowLayoutBinding.bind(holder.itemView).apply {

            txtAMPM.text = data?.get("amPm") as CharSequence?
            txtTSH.text = data?.get("timeStartHr") as CharSequence?
            txtTSM.text = data?.get("timeStartMin") as CharSequence?
            txtWFaculty.text = data?.get("facultyName") as CharSequence?
            txtWSubject.text = data?.get("subjectName") as CharSequence?
            txtWVenue.text = data?.get("roomNo") as CharSequence?

        }
    }

    override fun getItemCount(): Int {
        return schedule.size
    }


    fun setData(newData: List<DocumentSnapshot?>){
        this.schedule = newData as MutableList<DocumentSnapshot?>

        Log.d("ScheduleList", newData.size.toString())
    }
}