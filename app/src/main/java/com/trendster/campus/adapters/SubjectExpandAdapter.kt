package com.trendster.campus.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.trendster.campus.ui.fragment.subjects.update.ViewPdfActivity
import com.trendster.campus.utils.COLL_PDF_TITLE
import com.trendster.campus.utils.COLL_PDF_URL

class SubjectExpandAdapter(private val myContext: Context) : RecyclerView.Adapter<SubjectExpandAdapter.MyViewHolder>() {

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

                 val pdfUrl = data.get(COLL_PDF_URL) as String?
                 val pdfTitle = data.get(COLL_PDF_TITLE) as CharSequence?

                 txtItemTitle.text = pdfTitle
                 btnDownloadPdf.setOnClickListener {
                     val intent = Intent(Intent.ACTION_VIEW)
                     intent.data = Uri.parse(pdfUrl)
                     myContext.startActivity(intent)
                 }
                 rowLayout.setOnClickListener {

                     if (pdfUrl != null && pdfTitle != null){
                         val intent = Intent(myContext, ViewPdfActivity::class.java)
                         intent.putExtra(COLL_PDF_URL, pdfUrl)
                         intent.putExtra(COLL_PDF_TITLE, pdfTitle)
                         myContext.startActivity(intent)
                     }

                 }
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