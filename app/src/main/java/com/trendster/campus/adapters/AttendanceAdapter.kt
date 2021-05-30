package com.trendster.campus.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
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
                    setColors(
                        checkImage(position),
                        imageLayout,
                    )
                }
            }
        }
    }

    private fun setColors(
        myImage: Int,
        cardImages: ConstraintLayout,
    ) {
        cardImages.setBackgroundResource(myImage)
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
            1 -> R.drawable.ic_green_card
            2 -> R.drawable.ic_pink_card
            3 -> R.drawable.ic_purple_card
            4 -> R.drawable.ic_black_card
            5 -> R.drawable.ic_orange_card
            6 -> R.drawable.ic_yellow_card
            else -> R.drawable.ic_green_card
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
