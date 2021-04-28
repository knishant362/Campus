package com.trendster.campus.ui.fragment.subjects.subjecthome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.todkars.shimmer.ShimmerRecyclerView
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        _binding = FragmentSubjectsBinding.inflate(inflater, container, false)
        recyclerView = binding.subjectsRecyclerView
        mAdapter = SubjectsAdapter()
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = mAdapter
        recyclerView.showShimmer()

        auth.currentUser?.let { mainViewModel.loadRequest(it.uid, "subjects", "requiredDay") }

        Log.d("MYlist", auth.currentUser?.uid.toString())
        mainViewModel.readSubjects.observe(
            viewLifecycleOwner,
            {

                Log.d("MYlist", it.size.toString())
                mAdapter.setData(it)
                recyclerView.hideShimmer()
                mAdapter.notifyDataSetChanged()
            }
        )

        return binding.root
    }
}
