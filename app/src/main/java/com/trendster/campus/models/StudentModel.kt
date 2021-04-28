package com.trendster.campus.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class StudentModel(
    val studentName: String? = "Demo",
    val branch: String? = "Demo",
    val semester: String? = "Demo",
    val subjectName: String? = "Demo",
    val rollNo: String? = "Demo",
    val studentUID: String? = "Demo"
) : Parcelable, Serializable
