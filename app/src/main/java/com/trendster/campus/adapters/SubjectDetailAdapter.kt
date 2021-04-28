package com.trendster.campus.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.trendster.campus.R
import com.trendster.campus.databinding.SubjectDetailRowLayoutBinding
import com.trendster.campus.ui.fragment.subjects.subjecthome.SubjectCollectionFragmentDirections

class SubjectDetailAdapter : RecyclerView.Adapter<SubjectDetailAdapter.MyViewHolder>() {

    var collection = mutableListOf<DocumentSnapshot?>()
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

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

            setColors(holder.itemView.context, checkColor(position), collCard, txtCollTitle, imgColl)
        }

        holder.itemView.setOnClickListener { v ->
            Toast.makeText(holder.itemView.context, data, Toast.LENGTH_SHORT).show()
            val action = SubjectCollectionFragmentDirections.actionSubjectCollectionFragmentToCollectionExpandFragment(data!!)
            Navigation.findNavController(v).navigate(action)
        }
    }

    private fun setColors(
        context: Context,
        colorMap: HashMap<String, Any>,
        materialCardView: CardView,
        txtCollTitle: TextView,
        imgCollPoster: ImageView
    ) {
        val colorDark = colorMap["dark"].toString()
        val colorLight = colorMap["light"].toString()
        val image = colorMap["image"] as Int

        materialCardView.setCardBackgroundColor(Color.parseColor(colorLight))
        txtCollTitle.setTextColor(Color.parseColor(colorDark))
        imgCollPoster.setImageDrawable(
            ContextCompat.getDrawable(
                context, // Context
                image // Drawable
            )
        )
    }

    private fun checkColor(position: Int): HashMap<String, Any> {
        val colorMap: HashMap<String, Any> = HashMap()
        var data = if (position <4) {
            position
        } else {
            position % 4
        }
        when (data) {
            0 -> {
                colorMap["dark"] = "#00796B"
                colorMap["light"] = "#C7E6E4"
                colorMap["image"] = R.drawable.ic_coll_green
            }
            1 -> {
                colorMap["dark"] = "#4217A7"
                colorMap["light"] = "#D3C3EF"
                colorMap["image"] = R.drawable.ic_coll_purple
            }
            2 -> {
                colorMap["dark"] = "#C87E01"
                colorMap["light"] = "#F6D9AE"
                colorMap["image"] = R.drawable.ic_coll_yellow
            }
            3 -> {
                colorMap["dark"] = "#0374B2"
                colorMap["light"] = "#C8EBFB"
                colorMap["image"] = R.drawable.ic_coll_blue
            }
            4 -> {
                colorMap["dark"] = "#E64A19"
                colorMap["light"] = "#FFCCBC"
                colorMap["image"] = R.drawable.ic_subject_red
            }
        }
        return colorMap
    }

    override fun getItemCount(): Int {
        return collection.size
    }

    fun setData(newData: List<DocumentSnapshot?>) {
        this.collection = newData as MutableList<DocumentSnapshot?>

        Log.d("ScheduleList", newData.size.toString())
    }
}
