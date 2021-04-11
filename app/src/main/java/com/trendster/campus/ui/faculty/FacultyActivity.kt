package com.trendster.campus.ui.faculty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.trendster.campus.R
import com.trendster.campus.viewmodels.facultyviewmodel.FacultyViewModel
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModel
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModelFactory

class FacultyActivity : AppCompatActivity() {

    private lateinit var facultyViewModel: FacultyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty)

        facultyViewModel = ViewModelProvider(this).get(FacultyViewModel::class.java)

    }
}