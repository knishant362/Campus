package com.trendster.campus.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.trendster.campus.R
import com.trendster.campus.databinding.SubjectsRowLayoutBinding
import com.trendster.campus.ui.fragment.subjects.subjecthome.SubjectDetailsActivity
import com.trendster.campus.utils.FACULTY_NAME
import com.trendster.campus.utils.SUBJECT_DESC
import com.trendster.campus.utils.SUBJECT_NAME

class SubjectsAdapter : RecyclerView.Adapter<SubjectsAdapter.MyViewHolder>() {

    var subjects = mutableListOf<DocumentSnapshot?>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            intent.putExtra(SUBJECT_NAME, data!!.id)

            val facultyName = data[FACULTY_NAME]
            intent.putExtra(FACULTY_NAME, facultyName.toString())

            val subjectDesc = data[SUBJECT_DESC]
            intent.putExtra(SUBJECT_DESC, subjectDesc.toString())
            v?.context?.startActivity(intent)
        }

        SubjectsRowLayoutBinding.bind(holder.itemView).apply {
            txtSub.text = data!!.id
            Log.d("txtSub", subjects.size.toString())
            Log.d("txtSub", subjects.size.toString())

            setColors(holder.itemView.context, checkColor(position), subjectCard, txtSub, txtSubjectPoster)
        }
    }

    private fun setColors(
        context: Context,
        colorMap: HashMap<String, Any>,
        materialCardView: CardView,
        txtSubjectName: TextView,
        imgSubjectPoster: ImageView
    ) {
        val colorDark = colorMap["dark"].toString()
        val colorLight = colorMap["light"].toString()
        val image = colorMap["image"] as Int

//        materialCardView.setCardBackgroundColor(Color.parseColor(colorLight))
        txtSubjectName.setTextColor(Color.parseColor(colorDark))
        imgSubjectPoster.setImageDrawable(
            ContextCompat.getDrawable(
                context, // Context
                image // Drawable
            )
        )
    }

    private fun checkColor(position: Int): HashMap<String, Any> {
        val colorMap: HashMap<String, Any> = HashMap()
        var data = if (position <5) {
            position
        } else {
            position % 5
        }
        when (data) {
            0 -> {
                colorMap["dark"] = "#4217A7"
                colorMap["light"] = "#D3C3EF"
                colorMap["image"] = R.drawable.ic_subject_purple
            }
            1 -> {
                colorMap["dark"] = "#00796B"
                colorMap["light"] = "#C7E6E4"
                colorMap["image"] = R.drawable.ic_subject_green
            }
            2 -> {
                colorMap["dark"] = "#C87E01"
                colorMap["light"] = "#F6D9AE"
                colorMap["image"] = R.drawable.ic_subject_yellow
            }
            3 -> {
                colorMap["dark"] = "#0374B2"
                colorMap["light"] = "#C8EBFB"
                colorMap["image"] = R.drawable.ic_subject_blue
            }
            4 -> {
                colorMap["dark"] = "#E64A19"
                colorMap["light"] = "#FFCCBC"
                colorMap["image"] = R.drawable.ic_subject_red
            }
            5 -> {
                colorMap["dark"] = "#E64A19"
                colorMap["light"] = "#FFCCBC"
                colorMap["image"] = R.drawable.ic_subject_red
            }
        }
        return colorMap
    }

    override fun getItemCount(): Int {
        Log.d("itemCnt", subjects.size.toString())
        return subjects.size
    }

    fun setData(newData: List<DocumentSnapshot?>) {
        subjects = newData as MutableList<DocumentSnapshot?>
    }
}
