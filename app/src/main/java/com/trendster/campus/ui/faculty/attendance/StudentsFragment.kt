package com.trendster.campus.ui.faculty.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.trendster.campus.adapters.StudentListAdapter
import com.trendster.campus.databinding.FragmentStudentsBinding
import com.trendster.campus.viewmodels.facultyviewmodel.FacultyViewModel

class StudentsFragment : Fragment() {

    private var _binding: FragmentStudentsBinding? = null
    private val binding get() = _binding!!
    private val facultyViewModel: FacultyViewModel by activityViewModels()
    private lateinit var mAdapter: StudentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStudentsBinding.inflate(inflater, container, false)
        mAdapter = StudentListAdapter()
        binding.studentRecycler.adapter = mAdapter
        binding.studentRecycler.layoutManager = LinearLayoutManager(requireContext())

        facultyViewModel.fetchStudents(facultyViewModel.branchName, facultyViewModel.semName)

        facultyViewModel.listStudents.observe(
            viewLifecycleOwner,
            {
                mAdapter.setData(it, facultyViewModel.subName)
                mAdapter.notifyDataSetChanged()
            }
        )

        return binding.root
    }
}
