package com.trendster.campus.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.trendster.campus.R
import com.trendster.campus.databinding.NotificationRowLayoutBinding

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {

    private var notificationsList = mutableListOf<DocumentSnapshot?>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = parent.context.getSystemService(LayoutInflater::class.java)
            .inflate(R.layout.notification_row_layout, parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = notificationsList[position]?.data
        NotificationRowLayoutBinding.bind(holder.itemView).apply {

            val amPm = data?.get("amPm") as CharSequence?
            val hr = data?.get("hour") as CharSequence?
            val mint = data?.get("minute") as CharSequence?
            val time = "$hr:$mint $amPm"
            txtTimeDate.text = time
            txtNotifTitle.text = data?.get("title") as CharSequence?
            txtNotifDesc.text = data?.get("message") as CharSequence?
        }
    }

    override fun getItemCount(): Int {
        return notificationsList.size
    }

    fun setData(newData: List<DocumentSnapshot?>) {
        this.notificationsList = newData as MutableList<DocumentSnapshot?>

        Log.d("ScheduleList", newData.size.toString())
    }
}
