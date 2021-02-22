package com.trendster.campus.ui.fragment.schedule

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
import com.trendster.campus.adapters.ScheduleAdapter
import com.trendster.campus.databinding.FragmentScheduleBinding
import com.trendster.campus.viewmodels.MainViewModel

class ScheduleFragment : Fragment() {

    private var _binding : FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private val mainViewModel: MainViewModel by activityViewModels()
    lateinit var mAdapter: ScheduleAdapter
    lateinit var recyclerView: ShimmerRecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        recyclerView = binding.ScheduleRecyclerView
        mAdapter = ScheduleAdapter()
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.showShimmer()

        auth = FirebaseAuth.getInstance()
        auth.currentUser?.uid?.let { Log.d("FIREBASEUSER", it) }
        auth.currentUser?.let { mainViewModel.loadRequest(it.uid, "schedule", "requiredDay") }

        mainViewModel.readSchedule.observe(viewLifecycleOwner, {myData ->
            Log.d("FIREBASEUSER", myData.size.toString())
            mAdapter.setData(myData)
            recyclerView.hideShimmer()
            mAdapter.notifyDataSetChanged()
        })

        binding.fabAddSch.setOnClickListener {
            findNavController().navigate(R.id.action_scheduleFragment_to_updateScheduleFragment)
        }

        return binding.root
    }
}