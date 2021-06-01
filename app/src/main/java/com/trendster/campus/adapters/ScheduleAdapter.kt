package com.trendster.campus.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.trendster.campus.R
import com.trendster.campus.databinding.ScheduleRowLayoutBinding

class ScheduleAdapter(val onButtonClick: (String?) -> Unit) : RecyclerView.Adapter<ScheduleAdapter.MyViewHolder>() {

    private var schedule = mutableListOf<DocumentSnapshot?>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = parent.context.getSystemService(LayoutInflater::class.java)
            .inflate(R.layout.schedule_row_layout, parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = schedule[position]?.data
        ScheduleRowLayoutBinding.bind(holder.itemView).apply {
            val hour = (data?.get("timeStartHr") as Long?)
            val minute = (data?.get("timeStartMin") as Long?)
            if (hour != null && minute !== null) {
                timechip.text = timeString(hour, minute)
                Log.d("myTime", timeString(hour, minute) as String)
            }

            txtWFaculty.text = data?.get("facultyName") as String?
            val subjectName = data?.get("subjectName") as String?
            txtWSubject.text = subjectName
            txtWVenue.text = data?.get("roomNo") as String?

            chipLectureType.text = data?.get("TYPE") as String?
            chipLectureType.setOnClickListener { v ->
                if (!subjectName!!.contains("Lab"))
                onButtonClick(subjectName)
            }
        }
    }

    private fun checkColor(
        position: Int,
    ): HashMap<String, Int> {

        val colorMap: HashMap<String, Int> = HashMap()
        when (position) {
            1 -> {
                colorMap["light"] = R.color.p_purple_light
                colorMap["dark"] = R.color.p_purple_dark
            }
            2 -> {
                colorMap["light"] = R.color.p_green_light
                colorMap["dark"] = R.color.p_green_dark
            }
            3 -> {
                colorMap["light"] = R.color.p_red_light
                colorMap["dark"] = R.color.p_red_dark
            }
            4 -> {
                colorMap["light"] = R.color.p_blue_light
                colorMap["dark"] = R.color.p_blue_dark
            }
            else -> {
                colorMap["light"] = R.color.p_purple_light
                colorMap["dark"] = R.color.p_purple_dark
            }
        }
        return colorMap
    }

    private fun timeString(hour: Long, minute: Long): CharSequence? {
        val hr = if (hour > 12) {
            (hour - 12)
        } else hour
        val amPm = if (hour > 12) "PM" else "AM"
        val finalHr = if (hr.toString().length == 1)
            "0$hr"
        else hr.toString()
        val finalMint = if (minute.toString().length == 1)
            "0$minute"
        else minute.toString()

        return "$finalHr:$finalMint $amPm"
    }

    override fun getItemCount(): Int {
        return schedule.size
    }

    fun setData(newData: List<DocumentSnapshot?>) {
        this.schedule = newData as MutableList<DocumentSnapshot?>

        Log.d("ScheduleList", newData.size.toString())
    }
}
