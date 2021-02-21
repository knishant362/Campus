package com.trendster.campus.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.trendster.campus.R
import com.trendster.campus.databinding.ScheduleRowLayoutBinding
import com.trendster.campus.databinding.SubjectDetailRowLayoutBinding
import com.trendster.campus.ui.fragment.subjects.SubjectCollectionFragmentDirections
import com.trendster.campus.ui.fragment.subjects.SubjectDetailsActivity

class SubjectDetailAdapter: RecyclerView.Adapter<SubjectDetailAdapter.MyViewHolder>() {

    var collection = mutableListOf<DocumentSnapshot?>()
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = parent.context.getSystemService(LayoutInflater::class.java)
            .inflate(R.layout.subject_detail_row_layout, parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = collection[position]?.id
        SubjectDetailRowLayoutBinding.bind(holder.itemView).apply {
            txtCollTitle.text = data
            Log.d("ScheduleList11", data!!)
        }

        holder.itemView.setOnClickListener { v ->
            Toast.makeText(holder.itemView.context, data, Toast.LENGTH_SHORT).show()
            val action = SubjectCollectionFragmentDirections.actionSubjectCollectionFragmentToCollectionExpandFragment(data!!)
            Navigation.findNavController(v).navigate(action)
//            Toast.makeText(v.context, data, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return collection.size
    }


    fun setData(newData: List<DocumentSnapshot?>){
        this.collection = newData as MutableList<DocumentSnapshot?>

        Log.d("ScheduleList", newData.size.toString())
    }

}