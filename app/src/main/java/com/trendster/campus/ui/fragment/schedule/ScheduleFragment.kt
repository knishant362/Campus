package com.trendster.campus.ui.fragment.schedule

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val userUID = auth.currentUser?.uid

        setupAttendanceRecyclerView()
        setupScheduleRecyclerView()

        if (userUID != null) {
            mainViewModel.loadAttendance(userUID)
            mainViewModel.loadScheduleRequest(userUID, "today", mainViewModel.todayDay())
            mainViewModel.fetchUserName(userUID)
        }

        setupReadAttendanceObserver()

        setupReadScheduleObserver()

        setupLinkObserver()

        readUserNameObserver()

        setupWeekListener()

        return binding.root
    }

    private fun setupReadAttendanceObserver() {
        mainViewModel.readAttendance.observe(
            viewLifecycleOwner,
            { myData ->
                attendanceAdapter.setData(myData)
                attendanceAdapter.notifyDataSetChanged()
                Log.d("AttAdapter", myData.size.toString())
            }
        )
    }

    private fun setupReadScheduleObserver() {
        mainViewModel.readSchedule.observe(
            viewLifecycleOwner,
            { myData ->
                Log.d("FIREBASEUSER", myData.size.toString())
                if (myData.isEmpty()) {
                    recyclerView.hideShimmer()
                    placeHolderVisibility(View.VISIBLE)
                } else {
                    mAdapter.setData(myData)
                    recyclerView.hideShimmer()
                    mAdapter.notifyDataSetChanged()
                }
            }
        )
    }

    private fun readUserNameObserver() {
        mainViewModel.readUserName.observe(
            viewLifecycleOwner,
            { myData ->
                val displayText = "Hi, $myData"
                binding.studentName.text = displayText
            }
        )
    }

    private fun setupWeekListener() {
        binding.icWeekView.setOnClickListener {
            findNavController().navigate(R.id.action_scheduleFragment_to_weekScheduleFragment)
        }
    }

    private fun setupLinkObserver() {
        mainViewModel._readClassLink.value = "Reset"
        mainViewModel.readClassLink.observe(
            viewLifecycleOwner,
            {
                when (it) {
                    "null" -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), "Class Link is not added", Toast.LENGTH_LONG).show()
                    }
                    "Reset" -> {
                    }
                    else -> {
                        progressDialog.dismiss()
                        showAlertDialog(it)
                    }
                }
            }
        )
    }

    private fun setupAttendanceRecyclerView() {
        attendanceAdapter = AttendanceAdapter()
        attendanceRecyclerView = binding.attendanceRecycler
        attendanceRecyclerView.adapter = attendanceAdapter
        attendanceRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        setupProgressDialog()
    }

    private fun setupScheduleRecyclerView() {
        recyclerView = binding.scheduleRecycler
        mAdapter = ScheduleAdapter { subjectName ->
            if (subjectName != null) {
                progressDialog.show()
                mainViewModel.fetchClassLink(subjectName, "today")
            }
        }
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.showShimmer()
    }

    private fun setupProgressDialog() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading")
        progressDialog.setCanceledOnTouchOutside(false)
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun showAlertDialog(classlink: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Attend Class")
        builder.setMessage("Open: $classlink ?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            openUrl(classlink)
        }
        builder.setNeutralButton("Cancel") { dialogInterface, which ->
            Toast.makeText(requireContext(), "Not Attended", Toast.LENGTH_LONG).show()
        }
        builder.show()
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
