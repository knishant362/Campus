package com.trendster.campus.ui.faculty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.adapters.FacultyAdapter
import com.trendster.campus.databinding.FragmentFacultyBinding
import com.trendster.campus.viewmodels.facultyviewmodel.FacultyViewModel
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModel

class FacultyFragment : Fragment() {

    private var _binding : FragmentFacultyBinding? = null
    private val binding get() = _binding!!
    private val facultyViewModel: FacultyViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var facultyRecycler: RecyclerView
    private lateinit var mAdapter: FacultyAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentFacultyBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        facultyRecycler = binding.facultyRecycler
        mAdapter = FacultyAdapter()
        facultyRecycler.adapter = mAdapter
        facultyRecycler.layoutManager = LinearLayoutManager(requireContext())

        val faculty = auth.currentUser?.uid

        if (faculty != null) {
            facultyViewModel.fetchFacultySubjects(faculty)
        }

        facultyViewModel.readFacultySubjects.observe(viewLifecycleOwner, {myList ->
            mAdapter.setData(myList)
            mAdapter.notifyDataSetChanged()
        })

        return binding.root
    }

}