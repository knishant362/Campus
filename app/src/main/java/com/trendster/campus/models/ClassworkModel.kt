package com.trendster.campus.models

import java.io.Serializable

data class ClassworkModel(
    val assignmentDesc: String?,
    val assignmentTitle: String?,
    val dueDate: String?,
    val pdfName: String?,
    val pdfUrl: String?,
    val postDate: String?
) : Serializable
