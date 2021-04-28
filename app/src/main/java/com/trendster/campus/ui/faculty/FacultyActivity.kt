package com.trendster.campus.ui.faculty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.adapters.FacultyAdapter
import com.trendster.campus.databinding.ActivityFacultyBinding
import com.trendster.campus.viewmodels.facultyviewmodel.FacultyViewModel

class FacultyActivity : AppCompatActivity() {

    private lateinit var facultyViewModel: FacultyViewModel
    private var _binding: ActivityFacultyBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var facultyRecycler: RecyclerView
    private lateinit var mAdapter: FacultyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        facultyViewModel = ViewModelProvider(this).get(FacultyViewModel::class.java)

        auth = FirebaseAuth.getInstance()
        facultyRecycler = binding.facultyRecycler
        mAdapter = FacultyAdapter()
        facultyRecycler.adapter = mAdapter
        facultyRecycler.layoutManager = LinearLayoutManager(this)

        val faculty = auth.currentUser?.uid

        if (faculty != null) {
            facultyViewModel.fetchFacultySubjects(faculty)
        }

        facultyViewModel.readFacultySubjects.observe(
            this,
            { myList ->
                mAdapter.setData(myList)
                mAdapter.notifyDataSetChanged()
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
