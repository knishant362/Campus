package com.trendster.campus.ui.fragment.schedule

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.todkars.shimmer.ShimmerRecyclerView
import com.trendster.campus.adapters.ScheduleAdapter
import com.trendster.campus.databinding.FragmentWeekScheduleBinding
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModel

class WeekScheduleFragment : Fragment() {

    private var _binding: FragmentWeekScheduleBinding ? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var recyclerView: ShimmerRecyclerView
    private lateinit var mAdapter: ScheduleAdapter
    private lateinit var auth: FirebaseAuth
    private var day = "Monday"
    private lateinit var progressDialog: ProgressDialog
    private var myAuth: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWeekScheduleBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        myAuth = auth.currentUser?.uid

        setupRecyclerView()
        setupProgressDialog()

        if (myAuth != null)
            mainViewModel.loadScheduleRequest(myAuth!!, "week", day)

        setupReadScheduleWeek()

        setupChipListener()

        setupClassLinkObserver()

        return binding.root
    }

    private fun setupReadScheduleWeek() {
        mainViewModel.readScheduleWeek.observe(
            viewLifecycleOwner,
            {
                mAdapter.setData(it)
                mAdapter.notifyDataSetChanged()
                recyclerView.hideShimmer()
            }
        )
    }

    private fun setupChipListener() {
        binding.chipGroupDay.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedDay = chip.text.toString()
            if (myAuth != null) {
                day = selectedDay
                recyclerView.showShimmer()
                mainViewModel.loadScheduleRequest(myAuth!!, "week", day)
            }
        }
    }

    private fun setupClassLinkObserver() {
        mainViewModel._readClassLinkWeek.value = "Reset"
        mainViewModel.readClassLinkWeek.observe(
            viewLifecycleOwner,
            {
                if (it != null) {
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
            }
        )
    }

    private fun setupRecyclerView() {
        recyclerView = binding.weekRecyclerView
        mAdapter = ScheduleAdapter { subjectName ->
            if (subjectName != null) {
                progressDialog.show()
                mainViewModel.fetchClassLink(subjectName, "week")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
