package com.trendster.campus.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.adapters.ScheduleAdapter
import com.trendster.campus.databinding.FragmentWeekScheduleBinding
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModel

class WeekScheduleFragment : Fragment() {

    private var _binding : FragmentWeekScheduleBinding ? = null
    private val binding get() = _binding!!
    private val mainViewModel : MainViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: ScheduleAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWeekScheduleBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        recyclerView = binding.weekRecyclerView
        mAdapter = ScheduleAdapter()
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val myAuth = auth.currentUser?.uid
        mainViewModel.readSchedule.observe(viewLifecycleOwner, {
            mAdapter.setData(it)
            mAdapter.notifyDataSetChanged()

        })

        binding.chipGroupDay.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedDay = chip.text.toString()
            if (myAuth != null ){
                mainViewModel.loadRequest(myAuth, "week", selectedDay)
            }
        }

        return binding.root
    }
}