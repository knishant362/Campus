
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
import com.google.firebase.auth.FirebaseAuth
import com.todkars.shimmer.ShimmerRecyclerView
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
    private lateinit var recyclerView: ShimmerRecyclerView

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
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        Toast.makeText(requireContext(), category, Toast.LENGTH_SHORT).show()
        placeHolderVisibility(View.GONE)
        recyclerView.showShimmer()

        val userUID = auth.currentUser?.uid
        if (userUID != null) {
            subjectViewModel.loadCollExtend(userUID, category)
        }

        subjectViewModel.readCollExtend.observe(
            viewLifecycleOwner,
            { myData ->
                if (myData.isNotEmpty()) {
                    mAdapter.setData(myData)
                    mAdapter.notifyDataSetChanged()
                    placeHolderVisibility(View.GONE)
                    recyclerView.hideShimmer()
                } else {
                    placeHolderVisibility(View.VISIBLE)
                    recyclerView.hideShimmer()
                }
            }
        )

        return binding.root
    }

    private fun placeHolderVisibility(visibility: Int) {
        binding.imgNoData.visibility = visibility
        binding.txtNoData.visibility = visibility
    }
}
