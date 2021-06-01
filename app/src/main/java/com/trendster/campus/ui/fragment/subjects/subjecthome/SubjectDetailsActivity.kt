package com.trendster.campus.ui.fragment.subjects.subjecthome

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.R
import com.trendster.campus.databinding.ActivitySubjectDetailsBinding
import com.trendster.campus.utils.FACULTY_NAME
import com.trendster.campus.utils.SUBJECT_DESC
import com.trendster.campus.utils.SUBJECT_NAME
import com.trendster.campus.viewmodels.subjectviewmodel.SubjectViewModel

class SubjectDetailsActivity : AppCompatActivity() {

    private var _binding: ActivitySubjectDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var subjectViewModel: SubjectViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySubjectDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()

        subjectViewModel = ViewModelProvider(this).get(SubjectViewModel::class.java)

        val subject = intent.getStringExtra(SUBJECT_NAME)
        val faculty = intent.getStringExtra(FACULTY_NAME)
        val subjectDesc = intent.getStringExtra(SUBJECT_DESC)

        Toast.makeText(this, subject, Toast.LENGTH_SHORT).show()
        binding.closeButton.setOnClickListener {
            finish()
        }

        if (subject != null && faculty != null && subjectDesc != null) {
            auth.currentUser?.uid?.let { subjectViewModel.userAuthdata(subject, faculty, subjectDesc, it) }
            Log.d("heKL", subject)
        }
        navController = findNavController(R.id.nav_host_subject_content)
        binding.subjectBottomNav.setupWithNavController(navController)
    }
}
