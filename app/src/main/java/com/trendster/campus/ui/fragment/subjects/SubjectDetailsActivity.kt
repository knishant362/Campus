package com.trendster.campus.ui.fragment.subjects

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.R
import com.trendster.campus.viewmodels.subjectviewmodel.SubjectViewModel

class SubjectDetailsActivity : AppCompatActivity() {

    private lateinit var subjectViewModel: SubjectViewModel
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        auth = FirebaseAuth.getInstance()

        subjectViewModel = ViewModelProvider(this).get(SubjectViewModel::class.java)

        val subject = intent.getStringExtra("subjectName")
        if (subject != null) {
            auth.currentUser?.uid?.let { subjectViewModel.userAuthdata(subject, it) }
        }

    }
}