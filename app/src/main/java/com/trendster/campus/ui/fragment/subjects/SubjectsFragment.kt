package com.trendster.campus.ui.fragment.subjects

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
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSubjectsBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        mAdapter =   SubjectsAdapter()
        recyclerView = binding.subjectsRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = mAdapter

        auth.currentUser?.let { mainViewModel.loadRequest(it.uid, "subjects", "requiredDay") }

        Log.d("MYlist",auth.currentUser?.uid.toString())
        mainViewModel.readSubjects.observe(viewLifecycleOwner, {

            Log.d("MYlist", it.size.toString())
            mAdapter.setData(it)
            mAdapter.notifyDataSetChanged()

        })

        return binding.root
    }

}