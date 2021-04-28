 
package com.trendster.campus.ui.fragment.subjects.subjecthome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.adapters.SubjectExpandAdapter
import com.trendster.campus.databinding.FragmentCollectionExpandBinding
import com.trendster.campus.viewmodels.subjectviewmodel.SubjectViewModel

class CollectionExpandFragment : Fragment() {

    val args: CollectionExpandFragmentArgs by navArgs()
    private var _binding: FragmentCollectionExpandBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    private val subjectViewModel: SubjectViewModel by activityViewModels()
    private lateinit var mAdapter: SubjectExpandAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCollectionExpandBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val category = args.collCategory
        mAdapter = SubjectExpandAdapter()
        recyclerView = binding.expandRecyclerView

        Toast.makeText(requireContext(), category, Toast.LENGTH_SHORT).show()

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val userUID = auth.currentUser?.uid
        if (userUID != null) {
            subjectViewModel.loadCollExtend(userUID, category)
        }

        subjectViewModel.readCollExtend.observe(
            viewLifecycleOwner,
            { myData ->
                mAdapter.setData(myData)
                mAdapter.notifyDataSetChanged()
            }
        )

        return binding.root
    }
}
