package com.trendster.campus.ui.faculty.manageclass

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.trendster.campus.R
import com.trendster.campus.adapters.ClassworkAdapter
import com.trendster.campus.databinding.FragmentManageClassBinding
import com.trendster.campus.viewmodels.facultyviewmodel.FacultyViewModel

class ManageClassFragment : Fragment() {

    private var _binding: FragmentManageClassBinding? = null
    private val binding get() = _binding!!
    private val facultyViewModel: FacultyViewModel by activityViewModels()

    private lateinit var mAdapter: ClassworkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentManageClassBinding.inflate(inflater, container, false)

        mAdapter = ClassworkAdapter("faculty")
        binding.manageClassworkRecycler.adapter = mAdapter
        binding.manageClassworkRecycler.layoutManager = LinearLayoutManager(requireContext())

        Log.d("ajdljf", facultyViewModel.subName + facultyViewModel.branchName)

        facultyViewModel.loadClasswork()
        facultyViewModel.readClasswork.observe(
            viewLifecycleOwner,
            {
                mAdapter.setData(it)
                mAdapter.notifyDataSetChanged()
            }
        )

        binding.fabCreateClasswork.setOnClickListener {
            findNavController().navigate(R.id.action_manageClassFragment_to_createClassworkFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
