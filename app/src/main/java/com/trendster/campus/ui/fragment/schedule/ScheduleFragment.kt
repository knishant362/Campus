package com.trendster.campus.ui.fragment.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.adapters.ScheduleAdapter
import com.trendster.campus.databinding.FragmentScheduleBinding
import com.trendster.campus.viewmodels.MainViewModel

class ScheduleFragment : Fragment() {

    private var _binding : FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private val mainViewModel: MainViewModel by activityViewModels()
    lateinit var mAdapter: ScheduleAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        recyclerView = binding.ScheduleRecyclerView
        mAdapter = ScheduleAdapter()

        auth.currentUser?.uid?.let { Log.d("FIREBASEUSER", it) }
        auth.currentUser?.let { mainViewModel.loadRequest(it.uid, "schedule", "requiredDay") }

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mainViewModel.readSchedule.observe(viewLifecycleOwner, {myData ->
            Log.d("FIREBASEUSER", myData.size.toString())
            mAdapter.setData(myData)
            mAdapter.notifyDataSetChanged()
        })

        return binding.root
    }
}