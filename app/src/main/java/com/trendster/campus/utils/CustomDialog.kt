package com.trendster.campus.utils

import android.app.Activity
import android.app.AlertDialog
import com.trendster.campus.R

class CustomDialog(
    private val activity: Activity,
    private var dialog: AlertDialog.Builder
) {

    fun startLoading(){

        val inflater = activity.layoutInflater
        dialog.setView(inflater.inflate(R.layout.custom_progress_dialog, null))
        dialog.setCancelable(false)

        dialog.create()
        dialog.show()
    }

    fun dismiss(){
        dismiss()
    }
}