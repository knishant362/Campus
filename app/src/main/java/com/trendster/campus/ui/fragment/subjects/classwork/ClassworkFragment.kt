package com.trendster.campus.ui.fragment.subjects.classwork

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.adapters.ClassworkAdapter
import com.trendster.campus.databinding.FragmentClassworkBinding
import com.trendster.campus.viewmodels.subjectviewmodel.SubjectViewModel

class ClassworkFragment : Fragment() {

    private var _binding: FragmentClassworkBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAdapter: ClassworkAdapter
    private val subjectViewModel: SubjectViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentClassworkBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        mAdapter = ClassworkAdapter("student")
        binding.classworkRecycler.adapter = mAdapter
        binding.classworkRecycler.layoutManager = LinearLayoutManager(requireContext())

        subjectViewModel.loadRequest(auth.uid!!, subjectViewModel.selectedSubject)

        subjectViewModel.readClasswork.observe(
            viewLifecycleOwner,
            {
                mAdapter.setData(it)
                mAdapter.notifyDataSetChanged()
            }
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
