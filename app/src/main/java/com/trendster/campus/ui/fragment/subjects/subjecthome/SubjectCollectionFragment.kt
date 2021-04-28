package com.trendster.campus.ui.fragment.subjects.subjecthome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.adapters.SubjectDetailAdapter
import com.trendster.campus.databinding.FragmentSubjectCollectionBinding
import com.trendster.campus.viewmodels.subjectviewmodel.SubjectViewModel

class SubjectCollectionFragment : Fragment() {

    private var _binding: FragmentSubjectCollectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val subjectViewModel: SubjectViewModel by activityViewModels()
    private lateinit var mAdapter: SubjectDetailAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentSubjectCollectionBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        mAdapter = SubjectDetailAdapter()
        recyclerView = binding.subjectDetailsRecycler
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.txtDSubjectName.text = subjectViewModel.selectedSubject
        binding.txtDFacultyName.text = subjectViewModel.selectedFaculty
        binding.txtDSubjectDesc.text = subjectViewModel.selectedSubjectDesc

        val userUId = auth.currentUser?.uid
        Log.d("DetailLog", userUId.toString())

        if (userUId != null) {
            subjectViewModel.loadCollection(userUId, subjectViewModel.selectedSubject)
        }

        subjectViewModel.readCollection.observe(
            viewLifecycleOwner,
            { myData ->
                Log.d("SubCollection", myData.size.toString())
                mAdapter.setData(myData)
                mAdapter.notifyDataSetChanged()
            }
        )

        return binding.root
    }
}
