package com.trendster.campus.ui.faculty

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.trendster.campus.R
import com.trendster.campus.databinding.ActivityClassBinding
import com.trendster.campus.utils.SUBJECT_NAME
import com.trendster.campus.utils.USER_BRANCH
import com.trendster.campus.utils.USER_SEMESTER
import com.trendster.campus.viewmodels.facultyviewmodel.FacultyViewModel

class ClassActivity : AppCompatActivity() {

    private var _binding: ActivityClassBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var facultyViewModel: FacultyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.classNavHost)
        binding.classNavView.setupWithNavController(navController)

        facultyViewModel = ViewModelProvider(this).get(FacultyViewModel::class.java)

        val subName = intent.getStringExtra(SUBJECT_NAME)
        val branchName = intent.getStringExtra(USER_BRANCH)
        val semName = intent.getStringExtra(USER_SEMESTER)

        Log.d("ehl", subName.toString())

        if (subName != null && branchName != null && semName != null) {
            facultyViewModel.subName = subName
            facultyViewModel.branchName = branchName
            facultyViewModel.semName = semName
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
