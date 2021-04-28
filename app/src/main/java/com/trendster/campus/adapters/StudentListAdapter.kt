package com.trendster.campus.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.trendster.campus.R
import com.trendster.campus.databinding.StudentRowLayoutBinding
import com.trendster.campus.models.StudentModel
import com.trendster.campus.ui.faculty.attendance.StudentsFragmentDirections
import com.trendster.campus.utils.*

class StudentListAdapter : RecyclerView.Adapter<StudentListAdapter.MyViewHolder>() {

    var studentList = mutableListOf<DocumentSnapshot?>()
    var subjectName = "Demo"

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = parent.context.getSystemService(LayoutInflater::class.java)
            .inflate(R.layout.student_row_layout, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val docs = studentList[position]?.data
        Log.d("ScheduleList11", studentList.size.toString())
        StudentRowLayoutBinding.bind(holder.itemView).apply {

            val rollNo = docs?.get(USER_ROLL_NO) as String?
            val studentName = docs?.get(USER_NAME) as String?
            val studentUID = docs?.get(USER_UID) as String?

            txtStudentName.text = studentName
            txtRollNo.text = rollNo
//            txtPresentClasses.text = semName
//            txtTotalClasses.text = semName

            holder.itemView.setOnClickListener { v ->
                val action = StudentsFragmentDirections.actionStudentsFragmentToStudentAttendanceFragment(
                    StudentModel(subjectName = subjectName, studentName = studentName, studentUID = studentUID)
                )

                if (action != null) {
                    Navigation.findNavController(v).navigate(action)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun setData(newData: List<DocumentSnapshot?>, thisSubject: String) {
        this.studentList = newData as MutableList<DocumentSnapshot?>
        subjectName = thisSubject
        Log.d("SubjectExtend", newData.size.toString())
    }
}
