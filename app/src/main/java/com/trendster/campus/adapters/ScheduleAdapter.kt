package com.trendster.campus.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.trendster.campus.R
import com.trendster.campus.databinding.ScheduleRowLayoutBinding

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.MyViewHolder>() {

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

            txtWFaculty.text = data?.get("facultyName") as CharSequence?
            txtWSubject.text = data?.get("subjectName") as CharSequence?
            txtWVenue.text = data?.get("roomNo") as CharSequence?

            materialCardView2.radius = 14F
            lectureType.text = data?.get("TYPE") as CharSequence?

//            setColors(
//                checkColor(position, holder.itemView.context),
//                materialCardView2,
//                timechip
//            )
        }
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

    private fun setColors(
        colorMap: HashMap<String, Any>,
        materialCardView: CardView,
        timeChip: TextView
    ) {
        val colorDark = colorMap["dark"] as String
        val colorLight = colorMap["light"] as Int

//        materialCardView.setCardBackgroundColor(Color.parseColor(colorDark))
//        timeChip.setChipBackgroundColorResource(colorLight)
    }

    private fun checkColor(
        position: Int,
        context: Context
    ): HashMap<String, Any> {

        val colorMap: HashMap<String, Any> = HashMap()
        colorMap["dark"] = "#4217A7"
        colorMap["light"] = ContextCompat.getColor(context, R.color.card_blue_light)
        var pos = position + 1
        if (position> 5) {
            pos = position % 5
        }
        Log.d("MyChip", "$pos , $position")
        when (pos) {
            1 -> {
                colorMap["dark"] = "#375E97"
                colorMap["light"] = R.color.card_blue_light
            }
            2 -> {
                colorMap["dark"] = "#FB6542"
                colorMap["light"] = R.color.card_orange_light
            }
            3 -> {
                colorMap["dark"] = "#9BC01C"
                colorMap["light"] = R.color.card_green_light
            }
            4 -> {
                colorMap["dark"] = "#7E57C2"
                colorMap["light"] = R.color.card_purple_light
            }
            5 -> {
                colorMap["dark"] = "#DAA633"
                colorMap["light"] = R.color.card_yellow_light
            }
            else -> {
                colorMap["dark"] = "#4217A7"
                colorMap["light"] = R.color.card_blue_light
            }
        }
        return colorMap
    }

    override fun getItemCount(): Int {
        return schedule.size
    }

    fun setData(newData: List<DocumentSnapshot?>) {
        this.schedule = newData as MutableList<DocumentSnapshot?>

        Log.d("ScheduleList", newData.size.toString())
    }
}
