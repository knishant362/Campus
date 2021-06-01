package com.trendster.campus.ui.fragment.subjects.subjecthome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.todkars.shimmer.ShimmerRecyclerView
import com.trendster.campus.R
import com.trendster.campus.adapters.SubjectsAdapter
import com.trendster.campus.databinding.FragmentSubjectsBinding
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModel

class SubjectsFragment : Fragment() {

    private var _binding: FragmentSubjectsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var mAdapter: SubjectsAdapter
    private lateinit var recyclerView: ShimmerRecyclerView
    private var turn = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        _binding = FragmentSubjectsBinding.inflate(inflater, container, false)
        recyclerView = binding.subjectRecycler
        mAdapter = SubjectsAdapter()
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = mAdapter
        recyclerView.showShimmer()

        if (turn == 1) {
            auth.currentUser?.let {
                mainViewModel.loadRequest(it.uid)
                turn++
            }
        }

        Log.d("MYlist", auth.currentUser?.uid.toString())
        mainViewModel.readSubjects.observe(
            viewLifecycleOwner,
            {
                binding.txtBS.text = it.second
                if (it.first.isNotEmpty()) {
                    Log.d("MYlist", it.first.size.toString())
                    mAdapter.setData(it.first)
                    mAdapter.notifyDataSetChanged()
                    placeHolderVisibility(View.GONE)
                    recyclerView.hideShimmer()
                } else {
                    mAdapter.setData(mutableListOf())
                    mAdapter.notifyDataSetChanged()
                    recyclerView.hideShimmer()
                    placeHolderVisibility(View.VISIBLE)
                }
            }
        )

        binding.imgSortSubject.setOnClickListener {
            findNavController().navigate(R.id.action_subjectsFragment_to_sortSubjectFragment)
        }

        mainViewModel.readValues.observe(
            viewLifecycleOwner,
            {
                if (it.first == "null") {
                    recyclerView.showShimmer()
                } else {
                    placeHolderVisibility(View.GONE)
                    Toast.makeText(requireContext(), "Branch- ${it.first} , Semester: ${it.second}", Toast.LENGTH_SHORT).show()
                    mainViewModel.loadSubjects(it.first, it.second)
                }
            }
        )

        return binding.root
    }

    private fun placeHolderVisibility(visibility: Int) {
        binding.imgNoClasses.visibility = visibility
        binding.txtNoClasses.visibility = visibility
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
