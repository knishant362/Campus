package com.trendster.campus.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.trendster.campus.R
import com.trendster.campus.databinding.FacultyRowLayoutBinding
import com.trendster.campus.ui.faculty.ClassActivity
import com.trendster.campus.utils.SUBJECT_NAME
import com.trendster.campus.utils.USER_BRANCH
import com.trendster.campus.utils.USER_SEMESTER

class FacultyAdapter : RecyclerView.Adapter<FacultyAdapter.MyViewHolder>() {

    var faculty = mutableListOf<DocumentSnapshot?>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = parent.context.getSystemService(LayoutInflater::class.java)
            .inflate(R.layout.faculty_row_layout, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val docs = faculty[position]?.data
        Log.d("ScheduleList11", faculty.size.toString())
        FacultyRowLayoutBinding.bind(holder.itemView).apply {

            val subName = docs?.get("subName") as String?
            val branchName = docs?.get("branch") as String?
            val semName = docs?.get("semester") as String?

            txtSub.text = subName
            txtFacultyBranch.text = branchName
            txtFSem.text = semName

            holder.itemView.setOnClickListener { v ->
                Toast.makeText(holder.itemView.context, subName, Toast.LENGTH_SHORT).show()

                val intent = Intent(holder.itemView.context, ClassActivity::class.java)

                intent.putExtra(SUBJECT_NAME, subName)
                intent.putExtra(USER_BRANCH, branchName)
                intent.putExtra(USER_SEMESTER, semName)

                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return faculty.size
    }

    fun setData(newData: List<DocumentSnapshot?>) {
        this.faculty = newData as MutableList<DocumentSnapshot?>
        Log.d("SubjectExtend", newData.size.toString())
    }
}
