package com.trendster.campus.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.ramijemli.percentagechartview.callback.OnProgressChangeListener
import com.trendster.campus.R
import com.trendster.campus.databinding.ChartRowLayoutBinding

class AttendanceAdapter : RecyclerView.Adapter<AttendanceAdapter.MyViewHolder>()  {
    var attendance = mutableListOf<DocumentSnapshot?>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = parent.context.getSystemService(LayoutInflater::class.java)
            .inflate(R.layout.chart_row_layout, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val docs = attendance[position]
        Log.d("ScheduleList11", attendance.size.toString())
        ChartRowLayoutBinding.bind(holder.itemView).apply {

            if (docs != null){
                val myData = docs.data
                if (myData?.get("present") != null && myData["total"] != null){

                    val present = myData["present"] as Long
                    val total = myData["total"] as Long
                    val percentage = present.toFloat()/total.toFloat()
                    val showPercent = "$present/$total"
                    val inCirclePercent = "${percentage.times(100).toInt()}%"
                    txtAttPercent.text = inCirclePercent
                    txtAttendedClasses.text = showPercent
                    txtSubName.text = docs.id
                    viewId.progress = percentage
                    viewId.foregroundColor = ContextCompat.getColor(holder.itemView.context, R.color.light_blue_600)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return attendance.size
    }

    fun setData(newData: List<DocumentSnapshot?>){
        this.attendance = newData as MutableList<DocumentSnapshot?>
        Log.d("SubjectExtend", newData.size.toString())
    }

}