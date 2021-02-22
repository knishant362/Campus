package com.trendster.campus.ui.fragment.subjects

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.todkars.shimmer.ShimmerRecyclerView
import com.trendster.campus.R
import com.trendster.campus.adapters.SubjectsAdapter
import com.trendster.campus.databinding.FragmentSubjectsBinding
import com.trendster.campus.viewmodels.MainViewModel

class SubjectsFragment : Fragment() {

    private var _binding : FragmentSubjectsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var mAdapter : SubjectsAdapter
    private lateinit var recyclerView: ShimmerRecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        _binding = FragmentSubjectsBinding.inflate(inflater, container, false)

        recyclerView = binding.subjectsRecyclerView
        mAdapter =   SubjectsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = mAdapter
        recyclerView.showShimmer()

        auth.currentUser?.let { mainViewModel.loadRequest(it.uid, "subjects", "requiredDay") }

        Log.d("MYlist",auth.currentUser?.uid.toString())
        mainViewModel.readSubjects.observe(viewLifecycleOwner, {

            Log.d("MYlist", it.size.toString())
            mAdapter.setData(it)
            recyclerView.hideShimmer()
            mAdapter.notifyDataSetChanged()

        })

        binding.fabUpdateSubject.setOnClickListener {
            findNavController().navigate(R.id.action_subjectsFragment_to_updateSubjectFragment)
        }

        return binding.root
    }

}