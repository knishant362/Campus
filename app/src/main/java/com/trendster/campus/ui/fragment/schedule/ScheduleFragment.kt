package com.trendster.campus.ui.fragment.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.todkars.shimmer.ShimmerRecyclerView
import com.trendster.campus.R
import com.trendster.campus.adapters.AttendanceAdapter
import com.trendster.campus.adapters.ScheduleAdapter
import com.trendster.campus.databinding.FragmentScheduleBinding
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModel

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private val mainViewModel: MainViewModel by activityViewModels()
    lateinit var mAdapter: ScheduleAdapter
    lateinit var recyclerView: ShimmerRecyclerView

    lateinit var attendanceAdapter: AttendanceAdapter
    lateinit var attendanceRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        /** Attendance */
        attendanceAdapter = AttendanceAdapter()
        attendanceRecyclerView = binding.attendanceRecycler
        attendanceRecyclerView.adapter = attendanceAdapter
        attendanceRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        /** Schedule */
        recyclerView = binding.scheduleRecycler
        mAdapter = ScheduleAdapter()
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.showShimmer()

        auth = FirebaseAuth.getInstance()
        auth.currentUser?.uid?.let { Log.d("FIREBASEUSER", it) }
        auth.currentUser?.let { mainViewModel.loadScheduleRequest(it.uid, "today", mainViewModel.todayDay()) }
//        auth.currentUser?.let { mainViewModel.loadScheduleRequest(it.uid, "today", "Monday") }

        auth.currentUser?.let { mainViewModel.loadAttendance(it.uid) }
        mainViewModel.readAttendance.observe(
            viewLifecycleOwner,
            { myData ->
                attendanceAdapter.setData(myData)
                attendanceAdapter.notifyDataSetChanged()
                Log.d("AttAdapter", myData.size.toString())
            }
        )

        mainViewModel.readSchedule.observe(
            viewLifecycleOwner,
            { myData ->
                Log.d("FIREBASEUSER", myData.size.toString())
                if (myData.isEmpty()) {
                    recyclerView.hideShimmer()
                    binding.imgNoClasses.visibility = View.VISIBLE
                    binding.txtNoClasses.visibility = View.VISIBLE
                } else {
                    mAdapter.setData(myData)
                    recyclerView.hideShimmer()
                    mAdapter.notifyDataSetChanged()
                }
            }
        )

        binding.icWeekView.setOnClickListener {
            findNavController().navigate(R.id.action_scheduleFragment_to_weekScheduleFragment)
        }

        return binding.root
    }
}
