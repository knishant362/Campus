package com.trendster.campus.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.trendster.campus.R
import com.trendster.campus.databinding.ClassworkRowLayoutBinding
import com.trendster.campus.models.ClassworkModel
import com.trendster.campus.ui.fragment.subjects.classwork.ClassworkFragmentDirections
import com.trendster.campus.utils.*
import com.trendster.campus.utils.ASSIGNMENT_DESC
import jp.co.recruit_mp.android.circleprogressview.CircleProgressView

class ClassworkAdapter(private val usageType: String) : RecyclerView.Adapter<ClassworkAdapter.MyViewHolder>() {
    private var workList = mutableListOf<DocumentSnapshot?>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = parent.context.getSystemService(LayoutInflater::class.java)
            .inflate(R.layout.classwork_row_layout, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val docs = workList[position]
        Log.d("ScheduleList11", workList.size.toString())
        ClassworkRowLayoutBinding.bind(holder.itemView).apply {

            if (docs != null) {
                val myData = docs.data
                val assignmentTitle = myData?.get(ASSIGNMENT_TITLE) as String
                val assignmentDesc = myData[ASSIGNMENT_DESC] as String
                val dueDate = myData[ASSIGNMENT_DUE_DATE] as String
                val pdfName = myData[COLL_PDF_TITLE] as String
                val pdfUrl = myData[COLL_PDF_URL] as String?
                val postDate = myData[ASSIGNMENT_POST_DATE] as String

                txtWorkTitle.text = assignmentTitle

                when (usageType) {
                    "student" -> {
                        holder.itemView.setOnClickListener {
                            Log.d("Muss", "student")
                            val classwork = ClassworkModel(
                                assignmentDesc,
                                assignmentTitle,
                                dueDate,
                                pdfName,
                                pdfUrl,
                                postDate
                            )
                            val action = ClassworkFragmentDirections.actionClassworkFragmentToSubmissionFragment(classwork)
                            holder.itemView.findNavController().navigate(action)
                        }
                    }
                    "faculty" -> {
                        holder.itemView.setOnClickListener {
                            Log.d("Muss", "faculty")
                        }
                    }
                }
            }
        }
    }

    private fun setColors(
        context: Context,
        myColor: Pair<Int, Int>,
        attendanceChart: CircleProgressView,
        percentText: TextView
    ) {
        attendanceChart.foregroundColor = ContextCompat.getColor(context, myColor.first)
        attendanceChart.backgroundColor = ContextCompat.getColor(context, myColor.second)
        percentText.setTextColor(ContextCompat.getColor(context, myColor.first))
    }

    private fun checkColor(
        position: Int,
    ): Pair<Int, Int> {

        var pos = position + 1
        if (position> 5) {
            pos = position % 5
        }
        Log.d("MyChip", "$pos , $position")
        return when (pos) {
            1 -> Pair(R.color.card_blue_dark, R.color.card_blue_light)
            2 -> Pair(R.color.card_orange, R.color.card_orange_light)
            3 -> Pair(R.color.card_green, R.color.card_green_light)
            4 -> Pair(R.color.card_purple, R.color.card_purple_light)
            5 -> Pair(R.color.card_yellow, R.color.card_yellow_light)
            else -> Pair(R.color.card_blue_dark, R.color.card_blue_light)
        }
    }

    override fun getItemCount(): Int {
        return workList.size
    }

    fun setData(newData: List<DocumentSnapshot?>) {
        this.workList = newData as MutableList<DocumentSnapshot?>
        Log.d("SubjectExtend", newData.size.toString())
    }
}
