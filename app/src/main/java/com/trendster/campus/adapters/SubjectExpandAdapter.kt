package com.trendster.campus.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.trendster.campus.R
import com.trendster.campus.databinding.ExpandCollRowLayoutBinding
import com.trendster.campus.databinding.FragmentCollectionExpandBinding
import com.trendster.campus.databinding.SubjectDetailRowLayoutBinding
import com.trendster.campus.utils.COLL_PDF_TITLE

class SubjectExpandAdapter : RecyclerView.Adapter<SubjectExpandAdapter.MyViewHolder>() {

    var expandCollection = mutableListOf<DocumentSnapshot?>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = parent.context.getSystemService(LayoutInflater::class.java)
                .inflate(R.layout.expand_coll_row_layout, parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = expandCollection[position]
        ExpandCollRowLayoutBinding.bind(holder.itemView).apply {
             if (data != null) {
                 txtItemTitle.text = data.get(COLL_PDF_TITLE) as CharSequence?
             }
            if (data != null) {
                Log.d("ScheduleList11", (data.get(COLL_PDF_TITLE) as CharSequence?).toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return expandCollection.size
    }

    fun setData(newData: List<DocumentSnapshot?>){
        this.expandCollection = newData as MutableList<DocumentSnapshot?>

        Log.d("SubjectExtend", newData.size.toString())
    }

}