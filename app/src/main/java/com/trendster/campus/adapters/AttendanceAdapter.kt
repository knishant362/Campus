package com.trendster.campus.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.trendster.campus.R
import com.trendster.campus.databinding.ChartRowLayoutBinding

class AttendanceAdapter : RecyclerView.Adapter<AttendanceAdapter.MyViewHolder>() {
    var attendance = mutableListOf<DocumentSnapshot?>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = parent.context.getSystemService(LayoutInflater::class.java)
            .inflate(R.layout.chart_row_layout, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val docs = attendance[position]
        Log.d("ScheduleList11", attendance.size.toString())
        ChartRowLayoutBinding.bind(holder.itemView).apply {

            if (docs != null) {
                val myData = docs.data
                if (myData?.get("present") != null && myData["total"] != null) {

                    val present = myData["present"] as Long
                    val total = myData["total"] as Long
                    val percentage = present.toFloat() / total.toFloat()
                    val showPercent = "$present/$total"
                    val inCirclePercent = "${percentage.times(100).toInt()}%"
                    txtAttPercent.text = inCirclePercent
                    txtAttendedClasses.text = showPercent
                    txtSubName.text = docs.id
                    attendanceChart.progress = percentage
//                    setColors(
//                        holder.itemView.context,
//                        checkImage(position),
//                        cardImages,
//                        txtAttendedClasses
//                    )
                }
            }
        }
    }

    private fun setColors(
        context: Context,
        myImage: Int,
        cardImages: ImageView,
        percentText: TextView
    ) {
        cardImages.setImageResource(myImage)
    }

    private fun checkImage(
        position: Int,
    ): Int {
        var pos = position + 1
        if (position> 5) {
            pos = position % 5
        }
        Log.d("MyChip", "$pos , $position")
        return when (pos) {
//            1 -> Pair(R.color.card_blue_dark, R.color.card_blue_light)
//            2 -> Pair(R.color.card_orange, R.color.card_orange_light)
//            3 -> Pair(R.color.card_green, R.color.card_green_light)
//            4 -> Pair(R.color.card_purple, R.color.card_purple_light)
//            5 -> Pair(R.color.card_yellow, R.color.card_yellow_light)
            1 -> R.drawable.ic_1
            2 -> R.drawable.ic_2
            3 -> R.drawable.ic_7
            4 -> R.drawable.ic_4
            5 -> R.drawable.ic_5
            6 -> R.drawable.ic_6
            7 -> R.drawable.ic_3
            8 -> R.drawable.ic_8
            else -> R.drawable.ic_1
        }
    }

    override fun getItemCount(): Int {
        return attendance.size
    }

    fun setData(newData: List<DocumentSnapshot?>) {
        this.attendance = newData as MutableList<DocumentSnapshot?>
        Log.d("SubjectExtend", newData.size.toString())
    }
}
