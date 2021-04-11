package com.trendster.campus.ui.faculty

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.trendster.campus.R
import com.trendster.campus.adapters.StudentListAdapter
import com.trendster.campus.databinding.FragmentStudentsBinding
import com.trendster.campus.viewmodels.facultyviewmodel.FacultyViewModel

class StudentsFragment : Fragment() {

    private var _binding : FragmentStudentsBinding? = null
    private val binding get() = _binding!!
    private val facultyViewModel: FacultyViewModel by activityViewModels()
    private lateinit var mAdapter: StudentListAdapter
    val args: StudentsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStudentsBinding.inflate(inflater, container, false)
        mAdapter = StudentListAdapter()
        binding.studentRecycler.adapter = mAdapter
        binding.studentRecycler.layoutManager = LinearLayoutManager(requireContext())

        val studentInfo = args.studentInfo
        Toast.makeText(requireContext(), studentInfo.subjectName + studentInfo.branch + studentInfo.semester , Toast.LENGTH_SHORT).show()

        if (studentInfo.branch != null && studentInfo.semester != null){
            facultyViewModel.fetchStudents(studentInfo.branch, studentInfo.semester)
        }
        facultyViewModel.listStudents.observe(viewLifecycleOwner, {
            Log.d("HELL1", it.size.toString())
            studentInfo.subjectName?.let { it1 -> mAdapter.setData(it, it1) }
            mAdapter.notifyDataSetChanged()
        })
        
        return binding.root
    }

}