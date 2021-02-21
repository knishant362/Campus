package com.trendster.campus.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentWeekScheduleBinding
import com.trendster.campus.databinding.SubjectsRowLayoutBinding
import com.trendster.campus.ui.fragment.subjects.SubjectDetailsActivity
import com.trendster.campus.ui.fragment.subjects.SubjectsFragmentDirections

class SubjectsAdapter: RecyclerView.Adapter<SubjectsAdapter.MyViewHolder>() {

    var subjects = mutableListOf<DocumentSnapshot?>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        init {
//            itemView.setOnClickListener {
//                val position = adapterPosition
//                Toast.makeText(itemView.context, "Click ${position+1}", Toast.LENGTH_SHORT).show()
//            }
//        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = parent.context.getSystemService(LayoutInflater::class.java)
            .inflate(R.layout.subjects_row_layout, parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = subjects[position]
        holder.itemView.setOnClickListener { v ->
            val intent = Intent(v?.context, SubjectDetailsActivity::class.java)
            intent.putExtra("subjectName", data!!.id)
            v?.context?.startActivity(intent)
        }

        SubjectsRowLayoutBinding.bind(holder.itemView).apply {
            txtSub.text = data!!.id
            Log.d("txtSub", subjects.size.toString())
            Log.d("txtSub", subjects.size.toString())
        }
    }

    override fun getItemCount(): Int {
        Log.d("itemCnt", subjects.size.toString())
        return subjects.size
    }


    fun setData(newData: List<DocumentSnapshot?>){
        subjects = newData as MutableList<DocumentSnapshot?>

    }
}