package com.trendster.campus.adapters

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.trendster.campus.R
import com.trendster.campus.databinding.ExpandCollRowLayoutBinding
import com.trendster.campus.ui.fragment.subjects.ViewPdfActivity
import com.trendster.campus.utils.COLL_PDF_DESC
import com.trendster.campus.utils.COLL_PDF_TITLE
import com.trendster.campus.utils.COLL_PDF_URL

class SubjectExpandAdapter : RecyclerView.Adapter<SubjectExpandAdapter.MyViewHolder>() {

    var expandCollection = mutableListOf<DocumentSnapshot?>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = parent.context.getSystemService(LayoutInflater::class.java)
            .inflate(R.layout.expand_coll_row_layout, parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = expandCollection[position]
        ExpandCollRowLayoutBinding.bind(holder.itemView).apply {
            if (data != null) {

                val pdfUrl = data.get(COLL_PDF_URL) as CharSequence?
                val pdfTitle = data.get(COLL_PDF_TITLE) as CharSequence?
                val pdfDesc = data.get(COLL_PDF_DESC) as CharSequence?

                txtItemTitle.text = data.id
                txtItemDesc.text = pdfDesc
                if (pdfUrl != null) {
                    Log.d("HFHF", pdfUrl as String)
                    btnDownloadPdf.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(pdfUrl as String?)
                        holder.itemView.context.startActivity(intent)
                    }
                    cardItem.setOnClickListener {
                        Toast.makeText(holder.itemView.context, pdfTitle, Toast.LENGTH_SHORT).show()

//                     if (pdfUrl != null && pdfTitle != null){
                        val intent = Intent(holder.itemView.context, ViewPdfActivity::class.java)
                        intent.putExtra(COLL_PDF_URL, pdfUrl.toString())
                        intent.putExtra(COLL_PDF_TITLE, pdfTitle.toString())
                        holder.itemView.context.startActivity(intent)

//                     }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return expandCollection.size
    }

    fun setData(newData: List<DocumentSnapshot?>) {
        this.expandCollection = newData as MutableList<DocumentSnapshot?>

        Log.d("SubjectExtend", newData.size.toString())
    }
}
